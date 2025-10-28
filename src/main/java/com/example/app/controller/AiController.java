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
		return "ai_form"; // ← テンプレが templates 直下の場合
		// return "ai/ai_form"; // ← ai/ 配下に置いた場合はこちら
	}

	/** 提案実行 → 結果表示 */
	@PostMapping("/suggest")
	public String suggest(@RequestParam("prompt") String prompt,
			@RequestParam("festivalId") Integer festivalId, // フォームから受取
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
			return "ai_result"; // or "ai/ai_result"
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
			@SessionAttribute(name = "loginUser", required = false) User loginUser) {

		if (names == null || names.isEmpty()) {
			return "redirect:/ai/form";
		}

		if (loginUser == null) {
			return "redirect:/login";
		}
		if (festivalId == null) {
			return "redirect:/ai/form";
		}

		List<Integer> safeQty = (qty != null && qty.size() == names.size())
				? qty
				: Collections.nCopies(names.size(), 1);

		Integer userId = loginUser.getId();

		itemService.bulkInsertWithContext(names, safeQty, userId, festivalId, category);

		String redirectUrl = org.springframework.web.util.UriComponentsBuilder
				.fromPath("/item/list")
				.queryParam("festivalId", festivalId)
				.queryParam("page", 1)
				.build()
				.toUriString();
		return "redirect:" + redirectUrl;

	}

	/** 条件で自動提案（DBフィルタ版） */
	@PostMapping("/auto-suggest")
	public String autoSuggest(
			@RequestParam Integer festivalId,
			@RequestParam Integer days,
			@RequestParam String lodging,
			@RequestParam String gender,
			Model model) {

		model.addAttribute("festivals", festivalService.findAll());
		model.addAttribute("items",
				itemService.suggest(festivalId, days, lodging, gender));

		model.addAttribute("festivalId", festivalId);
		model.addAttribute("days", days);
		model.addAttribute("lodging", lodging);
		model.addAttribute("gender", gender);

		return "ai_autosuggest_result";
	}

	// 条件（festivalId, days, lodging, gender）からLLMに相談する
	@PostMapping("/suggest-by-cond")
	public String suggestByCond(
			@RequestParam Integer festivalId,
			@RequestParam Integer days,
			@RequestParam String lodging,
			@RequestParam String gender,
			Model model) {

		// 画面表示用（ai_result.htmlで使う）
		model.addAttribute("festivalId", festivalId);
		model.addAttribute("days", days);
		model.addAttribute("lodging", lodging);
		model.addAttribute("gender", gender);

		// プロンプト組み立て用にフェス名を取得
		String fesName = null;
		try {
			var fes = festivalService.findById(festivalId.longValue());
			if (fes != null)
				fesName = fes.getFesName();
		} catch (Exception ignore) {
		}

		// 条件 → プロンプト
		String prompt = """
				以下の条件に合う『持ち物リスト』を日本語で提案してください。
				- フェス名: %s
				- 参加日数: %d日
				- 宿泊手段: %s
				- 性別: %s
				出力形式:
				- 1行に1つ、品名のみ（説明文なし）
				- 最大50行
				""".formatted(fesName != null ? fesName : "(未指定)", days, lodging, gender);

		try {
			String result = aiService.askOnce(prompt);

			// 1行1品に正規化（番号・記号を落とす）
			List<String> lines = Arrays.stream(result.split("\\r?\\n"))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.map(s -> s.replaceFirst("^[\\p{Punct}\\s\\d]+", ""))
					.distinct()
					.limit(50)
					.collect(Collectors.toList());

			model.addAttribute("prompt", prompt);
			model.addAttribute("resultRaw", result);
			model.addAttribute("lines", lines);
			return "ai_result";
		} catch (Exception e) {
			model.addAttribute("prompt", prompt);
			model.addAttribute("resultRaw", "");
			model.addAttribute("lines", Collections.emptyList());
			model.addAttribute("error", "AI呼び出しでエラーが発生しました。しばらくして再実行してください。");
			return "ai_result";
		}
	}

}

