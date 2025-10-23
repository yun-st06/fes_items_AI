package com.example.app.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${memo.upload-dir}")
	private String uploadDir;

	//アップロードファイル用の設定
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path p = Paths.get(uploadDir).toAbsolutePath().normalize();
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations(p.toUri().toString());
	            .addResourceLocations("file:uploads/");
}}
