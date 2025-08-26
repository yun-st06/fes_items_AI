package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.FesMemo;
@Mapper
public interface FesMemoMapper {
	     void insert(FesMemo m);
	     List<FesMemo>findByUser(Integer userId);
	     FesMemo findByIdAndUser(@Param("id") Integer id,@Param("userId")Integer userId);
	     void update(FesMemo m);
	     void delete(@Param("id") Integer id,@Param("userId") Integer userId);
	   }
