package com.io;

public record AdminCreateRequest(
		String fullName,
		String username,
		String email,
		String password,
		String role,
		String photoUrl,
		Boolean active) {
}
