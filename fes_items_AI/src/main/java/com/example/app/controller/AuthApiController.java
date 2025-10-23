package com.example.app.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.User;
import com.example.app.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
public class AuthApiController {

    private final UserService userService;

    @PostMapping("/login") // 実URL: POST /api/login
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        User loginUser = userService.findByEmailAndPassword(email, password);

        if (loginUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        return ResponseEntity.ok(Map.of(
            "userId", loginUser.getId(),
            "name", loginUser.getName()
        ));
    }
}
	
	
	


