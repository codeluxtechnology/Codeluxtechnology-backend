package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

	Optional<AdminUser> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

	Optional<AdminUser> findByEmailIgnoreCase(String email);

	boolean existsByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

	List<AdminUser> findAllByOrderByCreatedAtDesc();
}
