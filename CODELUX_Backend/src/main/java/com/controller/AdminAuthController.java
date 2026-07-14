package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entity.AdminUser;
import com.io.AdminLoginRequest;
import com.io.AdminResponse;
import com.io.ApiResponse;
import com.io.LoginResponse;
import com.service.AdminService;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

	private final AdminService adminService;

	public AdminAuthController(AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody AdminLoginRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Login successful", adminService.login(request)));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<AdminResponse>> me(@AuthenticationPrincipal AdminUser adminUser) {
		return ResponseEntity.ok(ApiResponse.ok("Current admin", adminService.toResponse(adminUser)));
	}
}
