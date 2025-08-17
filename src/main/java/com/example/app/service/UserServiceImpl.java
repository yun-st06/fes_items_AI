package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.domain.User;
import com.example.app.mapper.UserMapper;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	
    
	@Override
    public User findByEmailAndPassword(String email, String password) {
        return userMapper.findByEmailAndPassword(email, password);
    }


	@Override
	public void register(User user) {
		userMapper.insert(user);

	}

	@Override
	public User findByEmail(String email) {
		
		return userMapper.findByEmail(email);
	}

	


}
