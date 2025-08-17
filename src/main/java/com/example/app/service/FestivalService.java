package com.example.app.service;

import java.util.List;

import com.example.app.domain.Festival;

public interface FestivalService {
      
	List<Festival> findAll();
	
	Festival findById(long id);
	
}
