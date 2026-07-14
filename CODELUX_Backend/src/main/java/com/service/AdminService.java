package com.service;

import java.util.List;
import java.util.Optional;

import com.entity.AdminUser;
import com.io.AdminCreateRequest;
import com.io.AdminLoginRequest;
import com.io.AdminResponse;
import com.io.LoginResponse;

public interface AdminService {

	LoginResponse bootstrap(AdminCreateRequest request);

	LoginResponse login(AdminLoginRequest request);

	Optional<AdminUser> findByToken(String rawToken);

	AdminResponse toResponse(AdminUser adminUser);

	List<AdminResponse> listAdmins();

	AdminResponse createAdmin(AdminCreateRequest request);

	AdminResponse updateAdmin(Long id, AdminCreateRequest request);

	void deleteAdmin(Long id);
}
