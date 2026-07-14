package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.MediaAsset;
import com.entity.MediaType;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {

	List<MediaAsset> findAllByOrderBySortOrderAscCreatedAtDesc();

	List<MediaAsset> findByActiveTrueOrderBySortOrderAscCreatedAtDesc();

	List<MediaAsset> findByMediaTypeAndActiveTrueOrderBySortOrderAscCreatedAtDesc(MediaType mediaType);
}
