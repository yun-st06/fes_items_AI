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
		if (u == null) throw new IllegalStateException("not logged in");
		return u.getId();

	}

	/* 一覧 */
	@GetMapping("/list")
	public String list(Model model, HttpSession session) {
		Integer uid = loginUserId(session);
		model.addAttribute("memos", memoService.list(uid));
		return "memo_list";

	}

	/* 登録画面 */
	@GetMapping("/new")
	public String newForm(Model model) {
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
			Model model) throws Exception {
		if (br.hasErrors()) {
			model.addAttribute("festivalList", festivalService.findAll());
			return "memo_new";

		}

		FesMemo m = new FesMemo();
		m.setFestivalId(form.getFestivalId());
		m.setMemoYear(form.getMemoYear());
		m.setMemoText(form.getMemoText());
		memoService.create(m, image, loginUserId(session));
		return "redirect:/memo/list";

	}

	/* 編集画面 */
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Integer id, HttpSession session, Model model) {
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
			Model model) throws Exception {
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
		return "redirect:/memo/list";

	}

	/* 削除 */
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, HttpSession session) throws Exception {
		memoService.delete(id, loginUserId(session));
		return "redirect:/mamo/list";

	}

}
