package com.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "site_settings")
public class SiteSettings {

	@Id
	private Long id = 1L;

	private String companyName = "Code Lux";

	private String tagline;

	private String aboutTitle;

	@Column(columnDefinition = "TEXT")
	private String aboutDescription;

	private String themePrimaryColor;

	private String themeSecondaryColor;

	private String logoUrl;

	private String faviconUrl;

	private String heroImageUrl;

	private String contactEmail;

	private String contactPhone;

	@Column(columnDefinition = "TEXT")
	private String address;

	private String facebookUrl;

	private String linkedinUrl;

	private String instagramUrl;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
