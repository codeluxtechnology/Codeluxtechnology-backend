package com.io;

public record SiteSettingsRequest(
		String companyName,
		String tagline,
		String aboutTitle,
		String aboutDescription,
	String themePrimaryColor,
	String themeSecondaryColor,
	String logoUrl,
	String faviconUrl,
	String heroImageUrl,
		String contactEmail,
		String contactPhone,
		String address,
		String facebookUrl,
		String linkedinUrl,
		String instagramUrl) {
}
