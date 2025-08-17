package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Festival;

@Mapper
public interface FestivalMapper {
	
	List<Festival>findAll();
	Festival findById(long id);
	}
