package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.ContactMessage;
import com.entity.ContactStatus;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

	List<ContactMessage> findAllByOrderByCreatedAtDesc();

	List<ContactMessage> findByStatusOrderByCreatedAtDesc(ContactStatus status);
}
