package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.SiteSettings;

public interface SiteSettingsRepository extends JpaRepository<SiteSettings, Long> {
}
