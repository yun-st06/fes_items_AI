package com.example.app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.app.domain.FesMemo;

public interface FesMemoService {
	
	List<FesMemo> list(Integer userId);
	FesMemo get(Integer id,Integer userId);
	void create(FesMemo memo,MultipartFile image,Integer userId)throws IOException;
	void update(FesMemo memo,MultipartFile image,Integer userId)throws IOException;
	void delete(Integer id,Integer userId)throws IOException;
}
