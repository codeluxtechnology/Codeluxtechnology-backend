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
@Table(name = "admin_users")
public class AdminUser extends BaseEntity {

	@Column(nullable = false)
	private String fullName;

	@Column(nullable = false, unique = true, length = 80)
	private String username;

	@Column(nullable = false, unique = true, length = 160)
	private String email;

	@Column(nullable = false)
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private AdminRole role = AdminRole.ADMIN;

	@Column(length = 1000)
	private String photoUrl;

	@Column(nullable = false)
	private boolean active = true;
}
