package com.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.entity.ContactMessage;
import com.entity.JobPost;
import com.entity.MediaAsset;
import com.entity.MediaType;
import com.entity.SiteSettings;
import com.entity.TeamMember;
import com.io.ApiResponse;
import com.io.ContactMessageRequest;
import com.service.WebsiteService;

@RestController
@RequestMapping("/api/public")
public class PublicWebsiteController {

	private final WebsiteService websiteService;

	public PublicWebsiteController(WebsiteService websiteService) {
		this.websiteService = websiteService;
	}

	@GetMapping("/settings")
	public ResponseEntity<ApiResponse<SiteSettings>> settings() {
		return ResponseEntity.ok(ApiResponse.ok("Site settings", websiteService.getSettings()));
	}

	@GetMapping("/media")
	public ResponseEntity<ApiResponse<List<MediaAsset>>> media(@RequestParam(required = false) String type) {
		return ResponseEntity.ok(ApiResponse.ok("Media assets", websiteService.listPublicMedia(parseMediaType(type))));
	}

	@GetMapping("/jobs")
	public ResponseEntity<ApiResponse<List<JobPost>>> jobs() {
		return ResponseEntity.ok(ApiResponse.ok("Active job posts", websiteService.listPublicJobs()));
	}

	@GetMapping("/team")
	public ResponseEntity<ApiResponse<List<TeamMember>>> team() {
		return ResponseEntity.ok(ApiResponse.ok("Team members", websiteService.listPublicTeam()));
	}

	@PostMapping("/contacts")
	public ResponseEntity<ApiResponse<ContactMessage>> submitContact(@RequestBody ContactMessageRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Message sent", websiteService.submitContact(request)));
	}

	private MediaType parseMediaType(String value) {
		if (!StringUtils.hasText(value)) {
			return null;
		}
		try {
			return MediaType.valueOf(value.trim().toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid media type");
		}
	}
}
