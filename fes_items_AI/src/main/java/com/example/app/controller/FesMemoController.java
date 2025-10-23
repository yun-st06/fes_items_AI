package com.example.app.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.FesMemo;
import com.example.app.domain.User;
import com.example.app.form.FesMemoForm;
import com.example.app.service.FesMemoService;
import com.example.app.service.FestivalService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/memo")
@RequiredArgsConstructor
public class FesMemoController {

	private final FesMemoService memoService;
	private final FestivalService festivalService; // 既存：一覧取得メソッド想定

	private Integer loginUserId(HttpSession session) {
		User u = (User) session.getAttribute("loginUser");
	    return (u != null) ? u.getId() : null;

	}

	/* 一覧 */
	@GetMapping("/list")
	public String list(Model model, HttpSession session) {
		Integer uid = loginUserId(session);
		if (uid == null) return "redirect:/login";   // ★ここでリダイレクト
		model.addAttribute("memos", memoService.list(uid));
		return "memo_list";

	}

	/* 登録画面 */
	@GetMapping("/new")
	public String newForm(Model model,HttpSession session) {
		Integer uid = loginUserId(session);
	    if (uid == null) return "redirect:/login"; // ★ 未ログインならログイン画面へ
		model.addAttribute("memoForm", new FesMemoForm());
		model.addAttribute("festivalList", festivalService.findAll());
		
		return "memo_new";

	}

	/* 登録処理 */
	@PostMapping("/new")
	public String create(@Valid @ModelAttribute("memoForm") FesMemoForm form,
			BindingResult br,
			@RequestParam(name = "image", required = false) MultipartFile image,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			Model model
			) throws Exception {
		if (br.hasErrors()) {
			model.addAttribute("festivalList", festivalService.findAll());
		    return "memo_new";

		}

		FesMemo m = new FesMemo();
		m.setFestivalId(form.getFestivalId());
		m.setMemoYear(form.getMemoYear());
		m.setMemoText(form.getMemoText());
		memoService.create(m, image, loginUserId(session));
		redirectAttributes.addFlashAttribute("message", "登録しました!");
		return "redirect:/memo/list";

	}

	/* 編集画面 */
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Integer id, HttpSession session, Model model) {
		Integer uid = loginUserId(session);
		if (uid == null) return "redirect:/login";
		
	    FesMemo m = memoService.get(id, loginUserId(session));
		FesMemoForm f = new FesMemoForm();
		f.setId(m.getId());
		f.setFestivalId(m.getFestivalId());
		f.setMemoYear(m.getMemoYear());
		f.setMemoText(m.getMemoText());
		model.addAttribute("memo", m);
		model.addAttribute("memoForm", f);
		model.addAttribute("festivalList", festivalService.findAll());
		return "memo_edit";

	}

	/* 更新処理 */
	@PostMapping("/edit/{id}")
	public String updata(@PathVariable Integer id,
			@Valid @ModelAttribute("memoForm") FesMemoForm form,
			BindingResult br,
			@RequestParam(name = "image", required = false) MultipartFile image,
			HttpSession session,
			RedirectAttributes redirectAttributes,
			Model model) throws Exception {
		Integer uid = loginUserId(session);
	    if (uid == null) return "redirect:/login"; 
		if (br.hasErrors()) {
			model.addAttribute("festivalList", festivalService.findAll());
			return "memo_edit";

		}

		FesMemo m = new FesMemo();
		m.setId(id);
		m.setFestivalId(form.getFestivalId());
		m.setMemoYear(form.getMemoYear());
		m.setMemoText(form.getMemoText());
		memoService.update(m, image, loginUserId(session));
		redirectAttributes.addFlashAttribute("message", "更新しました！");
		return "redirect:/memo/list";

	}

	/* 削除 */
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, 
		                 HttpSession session,
		                 RedirectAttributes redirectAttributes) throws Exception {
		Integer uid = loginUserId(session);
	    if (uid == null) return "redirect:/login";
		
		memoService.delete(id, loginUserId(session));
		redirectAttributes.addFlashAttribute("message", "削除しました！");
		
		return "redirect:/memo/list";

	}

}
