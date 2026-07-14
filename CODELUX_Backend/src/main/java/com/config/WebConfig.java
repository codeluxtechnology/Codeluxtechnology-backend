package com.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final Path uploadRoot;
	private final String publicPath;

	public WebConfig(@Value("${app.upload-dir:assets/uploads}") String uploadDir,
			@Value("${app.upload-public-path:/uploads}") String publicPath) {
		this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
		this.publicPath = publicPath.endsWith("/") ? publicPath.substring(0, publicPath.length() - 1) : publicPath;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(publicPath + "/**").addResourceLocations(uploadRoot.toUri().toString());
	}
}
