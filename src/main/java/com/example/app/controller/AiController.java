package com.example.app.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.app.ai.AiService;
import com.example.app.domain.User;
import com.example.app.service.FestivalService;
import com.example.app.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;
    private final ItemService itemService;
    private final FestivalService festivalService;

    /** フォーム表示（?q= 初期値可） */
    @GetMapping("/form")
    public String form(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("prompt", q == null ? "" : q);
        model.addAttribute("festivals", festivalService.findAll()); // ★ フェス一覧を追加
        return "ai_form";   // ← テンプレが templates 直下の場合
        // return "ai/ai_form"; // ← ai/ 配下に置いた場合はこちら
    }

    /** 提案実行 → 結果表示 */
    @PostMapping("/suggest")
    public String suggest(@RequestParam("prompt") String prompt,
                          @RequestParam("festivalId") Integer festivalId,  // フォームから受取
                          Model model) {
        try {
            String result = aiService.askOnce(prompt);

            List<String> lines = Arrays.stream(result.split("\\r?\\n"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.replaceFirst("^[\\p{Punct}\\s\\d]+", "")) // 先頭の記号/番号を除去
                    .distinct()
                    .limit(50)
                    .collect(Collectors.toList());

            model.addAttribute("prompt", prompt);
            model.addAttribute("resultRaw", result);
            model.addAttribute("lines", lines);
            model.addAttribute("festivalId", festivalId);
            return "ai_result";  // or "ai/ai_result"
        } catch (Exception e) {
            // 失敗時：画面でメッセージを出しつつ、空の結果を表示
            model.addAttribute("prompt", prompt);
            model.addAttribute("resultRaw", "");
            model.addAttribute("lines", Collections.emptyList());
            model.addAttribute("festivalId", festivalId);
            model.addAttribute("error",
                    "AI呼び出しでエラーが発生しました。時間をおいて再実行してください。"
                    + "（" + e.getClass().getSimpleName() + "）");
            return "ai_result";
        }
    }

    /** 一括登録 */
    @PostMapping("/register")
    public String register(
            @RequestParam(name = "names", required = false) List<String> names,
            @RequestParam(name = "qty", required = false) List<Integer> qty,
            @RequestParam(name = "category", required = false, defaultValue = "AI提案") String category,
            @RequestParam(name = "festivalId", required = false) Integer festivalId,
            @SessionAttribute(name = "loginUser", required = false) User loginUser
    ) {
        // ① 名前が空ならフォームに戻す
        if (names == null || names.isEmpty()) {
            return "redirect:/ai/form";
        }

        // ② ログインしていない場合はログインページへ
        if (loginUser == null) {
            return "redirect:/login";
        }

        // ③ フェスIDが未選択ならフォームに戻す（NOT NULL対応）
        if (festivalId == null) {
            return "redirect:/ai/form";
        }

        // ④ 数量チェック（null安全）
        List<Integer> safeQty = (qty != null && qty.size() == names.size())
                ? qty
                : Collections.nCopies(names.size(), 1);

        // ⑤ ログインユーザーのIDを取得（NOT NULL）
        Integer userId = loginUser.getId();

        // ⑥ 一括登録処理を呼び出し
        itemService.bulkInsertWithContext(names, safeQty, userId, festivalId, category);

        // ⑦ 完了したら「持ち物一覧」へ
        return "redirect:/item/list";
    }
}


