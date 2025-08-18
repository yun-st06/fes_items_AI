package com.example.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.User;
import com.example.app.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
   
   
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); 
        return "login"; 
    }
 
    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model,HttpSession session) {
        
        User loginUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (loginUser != null) {
            
            session.setAttribute("loginUser", loginUser);
            return "redirect:/item/list"; 
        } else {
            model.addAttribute("loginError", "メールアドレスまたはパスワードが違います");
            return "login";
        }
    }
    
    
    @GetMapping("/signup")
     public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; 
    }
    
   
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }
}
