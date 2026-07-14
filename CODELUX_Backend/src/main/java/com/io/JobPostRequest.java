package com.io;

import java.time.LocalDate;

public record JobPostRequest(
		String title,
		String location,
		String employmentType,
		String experience,
		String description,
		String requirements,
		String applyEmail,
		LocalDate closingDate,
		Boolean featured,
		Boolean active) {
}
