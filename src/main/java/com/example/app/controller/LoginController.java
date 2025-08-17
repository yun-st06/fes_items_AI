package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.User;
import com.example.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final HttpSession session;

    // 📘 ログイン画面の表示
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // 空のUserオブジェクトを渡す
        return "login"; // templates/login.html に遷移
    }

    // 📘 ログイン処理（POST）
    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        // メールアドレスとパスワードでユーザーを探す
        User loginUser = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (loginUser != null) {
            // ログイン成功 → セッションにユーザー情報を保存
            session.setAttribute("loginUser", loginUser);
            return "redirect:/item/list"; // ログイン成功後、一覧画面へリダイレクト
        } else {
            // ログイン失敗 → エラーメッセージを表示
            model.addAttribute("loginError", "メールアドレスまたはパスワードが違います");
            return "login";
        }
    }
    //サインアップ処理
    @GetMapping("/signup")
     public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // templates/signup.html を返す
 }
    
    // 📘 ログアウト処理
    @GetMapping("/logout")
    public String logout() {
        session.invalidate(); // セッション削除（ログアウト）
        return "redirect:/login";
    }
}
