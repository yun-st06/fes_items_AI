package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User{
	
	private Integer id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	
	}