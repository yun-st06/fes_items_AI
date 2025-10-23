package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.Festival;
import com.example.app.mapper.FestivalMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {
     
	private final FestivalMapper festivalMapper;
	
	@Override
	public List<Festival> findAll(){
		return festivalMapper.findAll();
		
	}
	
	@Override
	public Festival findById(long id) {
		return festivalMapper.findById(id);
		
		
	}
	
	
	
	
}
