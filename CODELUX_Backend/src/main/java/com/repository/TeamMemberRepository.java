package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.TeamMember;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

	List<TeamMember> findAllByOrderBySortOrderAscCreatedAtDesc();

	List<TeamMember> findByActiveTrueOrderBySortOrderAscCreatedAtDesc();
}
