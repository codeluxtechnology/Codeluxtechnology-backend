package com.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.io.AdminCreateRequest;
import com.io.AdminResponse;
import com.io.ApiResponse;
import com.service.AdminService;

@RestController
@RequestMapping("/api/admin/admins")
public class AdminManagementController {

	private final AdminService adminService;

	public AdminManagementController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AdminResponse>>> list() {
		return ResponseEntity.ok(ApiResponse.ok("Admins", adminService.listAdmins()));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<AdminResponse>> create(@RequestBody AdminCreateRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Admin created", adminService.createAdmin(request)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<AdminResponse>> update(@PathVariable Long id, @RequestBody AdminCreateRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Admin updated", adminService.updateAdmin(id, request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		adminService.deleteAdmin(id);
		return ResponseEntity.ok(ApiResponse.ok("Admin deleted"));
	}
}
