package com.io;

public record MediaAssetRequest(
		String title,
		String caption,
		String mediaType,
		String imageUrl,
		String linkUrl,
		Integer sortOrder,
		Boolean active) {
}
