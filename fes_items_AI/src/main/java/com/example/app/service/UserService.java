package com.example.app.service;

import com.example.app.domain.User;

public interface UserService {

	User findByEmailAndPassword(String email, String password);

	void register(User user);

	User findByEmail(String email);

	User register(String name, String email, String password);

}
