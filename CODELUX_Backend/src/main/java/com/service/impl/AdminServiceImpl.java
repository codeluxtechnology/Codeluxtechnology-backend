package com.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.entity.AdminRole;
import com.entity.AdminUser;
import com.io.AdminCreateRequest;
import com.io.AdminLoginRequest;
import com.io.AdminResponse;
import com.io.LoginResponse;
import com.repository.AdminUserRepository;
import com.service.AdminService;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	private final AdminUserRepository adminUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final SecureRandom secureRandom = new SecureRandom();
	private final long sessionHours;
	private final String tokenSecret;

	public AdminServiceImpl(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder,
			@Value("${app.security.admin-token-hours:12}") long sessionHours,
			@Value("${app.security.admin-token-secret}") String tokenSecret) {
		this.adminUserRepository = adminUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.sessionHours = sessionHours;
		this.tokenSecret = tokenSecret;
	}

	@Override
	public LoginResponse bootstrap(AdminCreateRequest request) {
		if (adminUserRepository.count() > 0) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin already exists. Use admin login.");
		}
		AdminUser admin = buildAdmin(request, AdminRole.SUPER_ADMIN);
		admin.setActive(true);
		return createToken(adminUserRepository.save(admin));
	}

	@Override
	public LoginResponse login(AdminLoginRequest request) {
		requireText(request.email(), "Email is required");
		requireText(request.password(), "Password is required");
		AdminUser admin = adminUserRepository
				.findByEmailIgnoreCase(request.email())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login details"));
		if (!admin.isActive() || !passwordEncoder.matches(request.password(), admin.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login details");
		}
		return createToken(admin);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AdminUser> findByToken(String rawToken) {
		if (!StringUtils.hasText(rawToken)) {
			return Optional.empty();
		}
		return parseAdminId(rawToken)
				.flatMap(adminUserRepository::findById)
				.filter(AdminUser::isActive);
	}

	@Override
	@Transactional(readOnly = true)
	public AdminResponse toResponse(AdminUser adminUser) {
		return new AdminResponse(adminUser.getId(), adminUser.getFullName(), adminUser.getUsername(),
				adminUser.getEmail(), adminUser.getRole(), adminUser.getPhotoUrl(), adminUser.isActive(),
				adminUser.getCreatedAt(), adminUser.getUpdatedAt());
	}

	@Override
	@Transactional(readOnly = true)
	public List<AdminResponse> listAdmins() {
		return adminUserRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
	}

	@Override
	public AdminResponse createAdmin(AdminCreateRequest request) {
		AdminUser admin = buildAdmin(request, parseRole(request.role(), AdminRole.ADMIN));
		return toResponse(adminUserRepository.save(admin));
	}

	@Override
	public AdminResponse updateAdmin(Long id, AdminCreateRequest request) {
		AdminUser admin = adminUserRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
		if (StringUtils.hasText(request.fullName())) {
			admin.setFullName(request.fullName().trim());
		}
		if (StringUtils.hasText(request.username()) && !request.username().equalsIgnoreCase(admin.getUsername())) {
			if (adminUserRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(request.username(), request.username())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
			}
			admin.setUsername(request.username().trim());
		}
		if (StringUtils.hasText(request.email()) && !request.email().equalsIgnoreCase(admin.getEmail())) {
			if (adminUserRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(request.email(), request.email())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
			}
			admin.setEmail(request.email().trim());
		}
		if (StringUtils.hasText(request.password())) {
			validatePassword(request.password());
			admin.setPasswordHash(passwordEncoder.encode(request.password()));
		}
		if (StringUtils.hasText(request.role())) {
			admin.setRole(parseRole(request.role(), admin.getRole()));
		}
		if (request.photoUrl() != null) {
			admin.setPhotoUrl(request.photoUrl());
		}
		if (request.active() != null) {
			admin.setActive(request.active());
		}
		return toResponse(adminUserRepository.save(admin));
	}

	@Override
	public void deleteAdmin(Long id) {
		AdminUser admin = adminUserRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
		if (admin.getRole() == AdminRole.SUPER_ADMIN && adminUserRepository.count() == 1) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "At least one admin must remain");
		}
		adminUserRepository.delete(admin);
	}

	private AdminUser buildAdmin(AdminCreateRequest request, AdminRole defaultRole) {
		requireText(request.fullName(), "Full name is required");
		requireText(request.username(), "Username is required");
		requireText(request.email(), "Email is required");
		requireText(request.password(), "Password is required");
		validatePassword(request.password());
		if (adminUserRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(request.username(), request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email already exists");
		}
		AdminUser admin = new AdminUser();
		admin.setFullName(request.fullName().trim());
		admin.setUsername(request.username().trim());
		admin.setEmail(request.email().trim());
		admin.setPasswordHash(passwordEncoder.encode(request.password()));
		admin.setRole(parseRole(request.role(), defaultRole));
		admin.setPhotoUrl(request.photoUrl());
		admin.setActive(request.active() == null || request.active());
		return admin;
	}

	private LoginResponse createToken(AdminUser admin) {
		LocalDateTime expiresAt = LocalDateTime.now().plusHours(sessionHours);
		long expiresEpochSeconds = expiresAt.atZone(ZoneId.systemDefault()).toEpochSecond();
		String nonce = randomUrlSafeText(24);
		String payload = admin.getId() + "." + expiresEpochSeconds + "." + nonce;
		return new LoginResponse(payload + "." + sign(payload), expiresAt, toResponse(admin));
	}

	private Optional<Long> parseAdminId(String rawToken) {
		String[] parts = rawToken.split("\\.");
		if (parts.length != 4) {
			return Optional.empty();
		}
		String payload = parts[0] + "." + parts[1] + "." + parts[2];
		if (!constantTimeEquals(sign(payload), parts[3])) {
			return Optional.empty();
		}
		try {
			long expiresEpochSeconds = Long.parseLong(parts[1]);
			if (expiresEpochSeconds <= Instant.now().getEpochSecond()) {
				return Optional.empty();
			}
			return Optional.of(Long.parseLong(parts[0]));
		} catch (NumberFormatException ex) {
			return Optional.empty();
		}
	}

	private String randomUrlSafeText(int bytes) {
		byte[] randomBytes = new byte[bytes];
		secureRandom.nextBytes(randomBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	}

	private String sign(String value) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(tokenSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin token could not be signed");
		}
	}

	private boolean constantTimeEquals(String expected, String actual) {
		return MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), actual.getBytes(StandardCharsets.UTF_8));
	}

	private AdminRole parseRole(String value, AdminRole fallback) {
		if (!StringUtils.hasText(value)) {
			return fallback;
		}
		try {
			return AdminRole.valueOf(value.trim().toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid admin role");
		}
	}

	private void requireText(String value, String message) {
		if (!StringUtils.hasText(value)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
		}
	}

	private void validatePassword(String password) {
		if (password.length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters");
		}
	}
}
