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

	@Override
	public User register(String name, String email, String password) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password); // ほんとはハッシュ化すべきだけど、まずは平文でOK

		userMapper.insert(user); // insert処理（戻り値なし想定）
		return user;

	}

	
}
