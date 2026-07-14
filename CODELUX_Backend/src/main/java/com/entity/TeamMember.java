package com.entity;

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
@Table(name = "team_members")
public class TeamMember extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String designation;

	@Column(columnDefinition = "TEXT")
	private String bio;

	@Column(length = 1000)
	private String imageUrl;

	private String linkedinUrl;

	@Column(nullable = false)
	private Integer sortOrder = 0;

	@Column(nullable = false)
	private boolean highlighted = false;

	@Column(nullable = false)
	private boolean active = true;
}
