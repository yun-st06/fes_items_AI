package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FesMemo {
	
	    private Integer id;
	    private Integer userId;
	    private Integer festivalId;
	    private Integer memoYear;
	    private String  memoText;
	    private String imagePath;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
	 
	    // 表示用
	    private String festivalName;
	    
    }
