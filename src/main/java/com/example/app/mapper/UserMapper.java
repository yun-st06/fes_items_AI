package com.example.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.User;

@Mapper
public interface UserMapper{
	   User findByEmail(String email);
	   int insert(User user);
	   User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}