package com.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "job_posts")
public class JobPost extends BaseEntity {

	@Column(nullable = false)
	private String title;

	private String location;

	private String employmentType;

	private String experience;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(columnDefinition = "TEXT")
	private String requirements;

	private String applyEmail;

	private String applyLink;

	private LocalDate closingDate;

	@Column(nullable = false)
	private boolean featured = false;

	@Column(nullable = false)
	private boolean active = true;
}
