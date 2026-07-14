package com.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entity.ContactMessage;
import com.entity.JobPost;
import com.entity.MediaAsset;
import com.entity.SiteSettings;
import com.entity.TeamMember;
import com.io.ApiResponse;
import com.io.ContactStatusRequest;
import com.io.JobPostRequest;
import com.io.MediaAssetRequest;
import com.io.SiteSettingsRequest;
import com.io.TeamMemberRequest;
import com.service.WebsiteService;

@RestController
@RequestMapping("/api/admin")
public class AdminContentController {

	private final WebsiteService websiteService;

	public AdminContentController(WebsiteService websiteService) {
		this.websiteService = websiteService;
	}

	@GetMapping("/settings")
	public ResponseEntity<ApiResponse<SiteSettings>> settings() {
		return ResponseEntity.ok(ApiResponse.ok("Site settings", websiteService.getSettings()));
	}

	@PutMapping("/settings")
	public ResponseEntity<ApiResponse<SiteSettings>> updateSettings(@RequestBody SiteSettingsRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Site settings updated", websiteService.updateSettings(request)));
	}

	@GetMapping("/media")
	public ResponseEntity<ApiResponse<List<MediaAsset>>> media() {
		return ResponseEntity.ok(ApiResponse.ok("Media assets", websiteService.listAllMedia()));
	}

	@PostMapping("/media")
	public ResponseEntity<ApiResponse<MediaAsset>> createMedia(@RequestBody MediaAssetRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Media asset created", websiteService.createMedia(request)));
	}

	@PutMapping("/media/{id}")
	public ResponseEntity<ApiResponse<MediaAsset>> updateMedia(@PathVariable Long id, @RequestBody MediaAssetRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Media asset updated", websiteService.updateMedia(id, request)));
	}

	@DeleteMapping("/media/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteMedia(@PathVariable Long id) {
		websiteService.deleteMedia(id);
		return ResponseEntity.ok(ApiResponse.ok("Media asset deleted"));
	}

	@GetMapping("/jobs")
	public ResponseEntity<ApiResponse<List<JobPost>>> jobs() {
		return ResponseEntity.ok(ApiResponse.ok("Job posts", websiteService.listAllJobs()));
	}

	@PostMapping("/jobs")
	public ResponseEntity<ApiResponse<JobPost>> createJob(@RequestBody JobPostRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Job post created", websiteService.createJob(request)));
	}

	@PutMapping("/jobs/{id}")
	public ResponseEntity<ApiResponse<JobPost>> updateJob(@PathVariable Long id, @RequestBody JobPostRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Job post updated", websiteService.updateJob(id, request)));
	}

	@DeleteMapping("/jobs/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
		websiteService.deleteJob(id);
		return ResponseEntity.ok(ApiResponse.ok("Job post deleted"));
	}

	@GetMapping("/team")
	public ResponseEntity<ApiResponse<List<TeamMember>>> team() {
		return ResponseEntity.ok(ApiResponse.ok("Team members", websiteService.listAllTeam()));
	}

	@PostMapping("/team")
	public ResponseEntity<ApiResponse<TeamMember>> createTeamMember(@RequestBody TeamMemberRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Team member created", websiteService.createTeamMember(request)));
	}

	@PutMapping("/team/{id}")
	public ResponseEntity<ApiResponse<TeamMember>> updateTeamMember(@PathVariable Long id,
			@RequestBody TeamMemberRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Team member updated", websiteService.updateTeamMember(id, request)));
	}

	@DeleteMapping("/team/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteTeamMember(@PathVariable Long id) {
		websiteService.deleteTeamMember(id);
		return ResponseEntity.ok(ApiResponse.ok("Team member deleted"));
	}

	@GetMapping("/contacts")
	public ResponseEntity<ApiResponse<List<ContactMessage>>> contacts(@RequestParam(required = false) String status) {
		return ResponseEntity.ok(ApiResponse.ok("Contact messages", websiteService.listContacts(status)));
	}

	@PatchMapping("/contacts/{id}/status")
	public ResponseEntity<ApiResponse<ContactMessage>> updateContactStatus(@PathVariable Long id,
			@RequestBody ContactStatusRequest request) {
		return ResponseEntity.ok(ApiResponse.ok("Contact status updated",
				websiteService.updateContactStatus(id, request.status())));
	}

	@DeleteMapping("/contacts/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteContact(@PathVariable Long id) {
		websiteService.deleteContact(id);
		return ResponseEntity.ok(ApiResponse.ok("Contact message deleted"));
	}
}
