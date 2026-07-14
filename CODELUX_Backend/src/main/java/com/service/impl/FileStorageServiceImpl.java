package com.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.service.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path uploadRoot;
	private final String publicPath;
	private final String cloudName;
	private final String apiKey;
	private final String apiSecret;
	private final String cloudinaryRootFolder;
	private final RestClient restClient;

	public FileStorageServiceImpl(@Value("${app.upload-dir:assets/uploads}") String uploadDir,
			@Value("${app.upload-public-path:/uploads}") String publicPath,
			@Value("${app.cloudinary.cloud-name:}") String cloudName,
			@Value("${app.cloudinary.api-key:}") String apiKey,
			@Value("${app.cloudinary.api-secret:}") String apiSecret,
			@Value("${app.cloudinary.root-folder:codelux}") String cloudinaryRootFolder) {
		this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
		this.publicPath = publicPath.endsWith("/") ? publicPath.substring(0, publicPath.length() - 1) : publicPath;
		this.cloudName = cloudName;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.cloudinaryRootFolder = sanitizeFolder(cloudinaryRootFolder);
		this.restClient = RestClient.create();
	}

	@Override
	public String storeImage(MultipartFile file, String folder) {
		if (file == null || file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file is required");
		}
		String contentType = file.getContentType();
		if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed");
		}
		if (isCloudinaryConfigured()) {
			return storeCloudinaryImage(file, folder);
		}
		String safeFolder = sanitizeFolder(folder);
		String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
		String extension = "";
		int dotIndex = originalName.lastIndexOf('.');
		if (dotIndex >= 0 && dotIndex < originalName.length() - 1) {
			extension = "." + originalName.substring(dotIndex + 1).replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
		}
		String fileName = UUID.randomUUID() + extension;
		try {
			Path targetDir = uploadRoot.resolve(safeFolder).normalize();
			if (!targetDir.startsWith(uploadRoot)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid upload folder");
			}
			Files.createDirectories(targetDir);
			Files.copy(file.getInputStream(), targetDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
			return publicPath + "/" + safeFolder + "/" + fileName;
		} catch (IOException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save image", ex);
		}
	}

	private String storeCloudinaryImage(MultipartFile file, String folder) {
		String safeFolder = sanitizeFolder(folder);
		String cloudinaryFolder = cloudinaryRootFolder + "/" + safeFolder;
		long timestamp = Instant.now().getEpochSecond();
		String signature = sha1("folder=" + cloudinaryFolder + "&timestamp=" + timestamp + apiSecret);
		try {
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", filePart(file));
			body.add("folder", cloudinaryFolder);
			body.add("timestamp", String.valueOf(timestamp));
			body.add("api_key", apiKey);
			body.add("signature", signature);

			@SuppressWarnings("unchecked")
			Map<String, Object> response = restClient.post()
					.uri("https://api.cloudinary.com/v1_1/{cloudName}/image/upload", cloudName)
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.body(body)
					.retrieve()
					.body(Map.class);
			Object secureUrl = response == null ? null : response.get("secure_url");
			if (secureUrl == null || !StringUtils.hasText(secureUrl.toString())) {
				throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Cloudinary did not return an image URL");
			}
			return secureUrl.toString();
		} catch (IOException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not read image for Cloudinary", ex);
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Cloudinary upload failed", ex);
		}
	}

	private HttpEntity<ByteArrayResource> filePart(MultipartFile file) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		String contentType = file.getContentType();
		if (StringUtils.hasText(contentType)) {
			headers.setContentType(MediaType.parseMediaType(contentType));
		}
		return new HttpEntity<>(new MultipartFileResource(file), headers);
	}

	private boolean isCloudinaryConfigured() {
		return StringUtils.hasText(cloudName) && StringUtils.hasText(apiKey) && StringUtils.hasText(apiSecret);
	}

	private String sha1(String value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("SHA-1 is not available", ex);
		}
	}

	private String sanitizeFolder(String folder) {
		if (!StringUtils.hasText(folder)) {
			return "general";
		}
		return folder.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_-]", "-");
	}

	private static final class MultipartFileResource extends ByteArrayResource {

		private final String filename;

		private MultipartFileResource(MultipartFile file) throws IOException {
			super(file.getBytes());
			this.filename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "image" : file.getOriginalFilename());
		}

		@Override
		public String getFilename() {
			return filename;
		}
	}
}
