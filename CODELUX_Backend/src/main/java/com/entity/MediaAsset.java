package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "media_assets")
public class MediaAsset extends BaseEntity {

	@Column(nullable = false)
	private String title;

	private String caption;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 40)
	private MediaType mediaType = MediaType.GALLERY;

	@Column(nullable = false)
	private String imageUrl;

	private String linkUrl;

	@Column(nullable = false)
	private Integer sortOrder = 0;

	@Column(nullable = false)
	private boolean active = true;
}
