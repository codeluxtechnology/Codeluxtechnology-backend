package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.io.ApiResponse;
import com.io.UploadResponse;
import com.service.FileStorageService;

@RestController
@RequestMapping("/api/admin/uploads")
public class UploadController {

	private final FileStorageService fileStorageService;

	public UploadController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	@PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<UploadResponse>> uploadImage(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "folder", defaultValue = "general") String folder) {
		String url = fileStorageService.storeImage(file, folder);
		return ResponseEntity.ok(ApiResponse.ok("Image uploaded", new UploadResponse(url)));
	}
	
	
}
