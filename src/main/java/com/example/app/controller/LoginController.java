package com.example.app.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.User;
import com.example.app.form.LoginForm;
import com.example.app.form.SignupForm;
import com.example.app.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
   
   //ログイン画面表示
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm()); 
        return "login"; 
    }
    
    //ログイン処理
    @PostMapping("/login")
    public String login(@ModelAttribute("loginForm") @Valid LoginForm form,
    		              BindingResult result,
                          HttpSession session,
                          Model model) {
        
    	if(result.hasErrors()) {
    		return "login";
    	}
    	
    	//メール＆パスワードでユーザー取得
        User loginUser = userService.findByEmailAndPassword(form.getEmail(), form.getPassword());
        if (loginUser != null) {
        	
        	// ログ出力
            System.out.println("ログイン成功: " + loginUser.getId() + ", " + loginUser.getName());
        	
            session.setAttribute("loginUser", loginUser);
            //return "redirect:/item/list";
            return "redirect:/ai/form";
            //return "redirect:/memo/list";
            
        } else {
            model.addAttribute("loginError", "メールアドレスまたはパスワードが違います");
            return "login";
        }
    }
    
    
    //サインアップ画面表示
    @GetMapping("/signup")
     public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "signup"; 
    }
        
    
    //サインアップ処理
    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupForm") @Valid SignupForm form,
    		              BindingResult result,
    		              RedirectAttributes redirectAttributes,
    		              HttpSession session,
    		              Model model) {
    
    	if(result.hasErrors()) {
    		return "signup";
    		
    	}
    	
    
    	
     // 登録（User作成して保存 → 戻り値で作成ユーザーを受け取る想定）
     User created =userService.register(form.getName(),form.getEmail(),form.getPassword());
     
     
     // そのままログイン状態にする場合（任意）
     session.setAttribute("loginUser", created);
     
     // 一覧にフラッシュメッセージ 
     redirectAttributes.addFlashAttribute("message","新規アカウント登録されました");
     
     return "redirect:/item/list";
     
    	
     }
    
  
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }
}
