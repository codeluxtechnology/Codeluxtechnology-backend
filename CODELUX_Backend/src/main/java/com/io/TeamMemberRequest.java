package com.io;

public record TeamMemberRequest(
		String name,
		String designation,
		String bio,
		String imageUrl,
		String linkedinUrl,
		Integer sortOrder,
		Boolean highlighted,
		Boolean active) {
}
