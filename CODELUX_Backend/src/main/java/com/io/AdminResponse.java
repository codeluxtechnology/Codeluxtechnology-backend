package com.io;

import java.time.LocalDateTime;

import com.entity.AdminRole;

public record AdminResponse(
		Long id,
		String fullName,
		String username,
		String email,
		AdminRole role,
		String photoUrl,
		boolean active,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
