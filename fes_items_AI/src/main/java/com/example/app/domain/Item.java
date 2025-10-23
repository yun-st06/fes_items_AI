package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Item{
	 
	private Integer id;
	private Integer userId;
	private Integer festivalId;
	private String festivalName;
	private String itemName;
	private String category;
	private Integer quantity;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}