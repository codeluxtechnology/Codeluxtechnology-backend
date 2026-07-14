package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.JobPost;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

	List<JobPost> findAllByOrderByCreatedAtDesc();

	List<JobPost> findByActiveTrueOrderByFeaturedDescCreatedAtDesc();
}
