package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.entity.AdminRole;
import com.entity.AdminUser;
import com.repository.AdminUserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

	private final AdminUserRepository adminUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final boolean enabled;
	private final String email;
	private final String username;
	private final String fullName;
	private final String password;

	public AdminInitializer(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder,
			@Value("${app.admin.initializer.enabled:true}") boolean enabled,
			@Value("${app.admin.initializer.email:rupnar8459@gmail.com}") String email,
			@Value("${app.admin.initializer.username:admin}") String username,
			@Value("${app.admin.initializer.full-name:Super Admin}") String fullName,
			@Value("${app.admin.initializer.password:admin123}") String password) {
		this.adminUserRepository = adminUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.enabled = enabled;
		this.email = email;
		this.username = username;
		this.fullName = fullName;
		this.password = password;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (!enabled) {
			logger.info("Admin initializer is disabled.");
			return;
		}
		if (adminUserRepository.count() > 0) {
			logger.info("Admin already exists. Skipping default admin creation.");
			return;
		}

		AdminUser admin = new AdminUser();
		admin.setFullName(fullName);
		admin.setUsername(username);
		admin.setEmail(email);
		admin.setPasswordHash(passwordEncoder.encode(password));
		admin.setRole(AdminRole.SUPER_ADMIN);
		admin.setActive(true);

		adminUserRepository.save(admin);
		logger.info("Default SUPER_ADMIN created: {} ({})", fullName, email);
	}
}
