package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.User;
import com.example.app.service.UserService;

import jakarta.servlet.http.HttpSession;
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
    
    @PostMapping("/registar")
    public String register(@ModelAttribute @Validated UserForm form,
    		               BindingResult result,
    		               RedirectAttributes redirectAttributes) {
    	if(result.hasErrors()) {
    		return "signup";
    		
    	}
    	
    	userService.register(form);
    	redirectAttributes.addFlashAttribute("message","新規アカウント登録完了しました")
    	return "redirect:/item/list";
    }
    
    
    
    
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }
}
