package com.example.app.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Item;
import com.example.app.domain.User;
import com.example.app.form.ItemForm;
import com.example.app.service.FestivalService;
import com.example.app.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	private final FestivalService festivalService;

	//持ち物一覧表示
	@GetMapping("/list")
	public String listItems(@RequestParam(required = false) String category,
			@RequestParam(required = false) Integer festivalId,
			Model model, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("loginUser", loginUser);
		model.addAttribute("festivalList", festivalService.findAll());

		if ((category != null && !category.isEmpty()) ||
				(festivalId != null)) {
			model.addAttribute("itemList", itemService.search(category, festivalId));
		} else {
			model.addAttribute("itemList", itemService.findAll());

		}

		return "item_list";
	}

	//持ち物新規登録
	@GetMapping("/new")
	public String showNewForm(Model model) {
		model.addAttribute("itemForm", new ItemForm());
		model.addAttribute("festivalList", festivalService.findAll());
		return "item_new";
	}

	//持ち物新規登録のエラー表示
	@PostMapping("/new")
	public String register(@Validated ItemForm form,
			BindingResult result,
			Model model,
			HttpSession session) {

		if (result.hasErrors()) {
			model.addAttribute("festivalList", festivalService.findAll());
			return "item_new";
		}

		User loginUser = (User) session.getAttribute("loginUser");

		//DTOからDBへ登録処理
		Item item = new Item();
		item.setItemName(form.getItemName());
		item.setCategory(form.getCategory());
		item.setQuantity(form.getQuantity());
		item.setFestivalId(form.getFestivalId());
		item.setUserId(loginUser.getId());

		itemService.insert(item);
		return "redirect:/item/List";
	}

	//持ち物編集
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
		Item item = itemService.findById(id);

		//DBからDTOへ登録
		ItemForm form = new ItemForm();
		form.setId(item.getId());
		form.setItemName(item.getItemName());
		form.setCategory(item.getCategory());
		form.setQuantity(item.getQuantity());
		form.setFestivalId(item.getFestivalId());

		model.addAttribute("itemForm", form);
		model.addAttribute("festivalList", festivalService.findAll());
		return "item_edit";
	}

	//持ち物編集のエラー表示
	@PostMapping("/edit/{id}")
	public String update(@Validated ItemForm form, BindingResult result,
			Model model, HttpSession session) {

		if (result.hasErrors()) {
			model.addAttribute("festivalList", festivalService.findAll());
			return "item_edit";

		}

		User loginUser = (User) session.getAttribute("loginUser");

		//再度DTOからDBへ登録処理
		Item item = new Item();
		item.setId(form.getId());
		item.setItemName(form.getItemName());
		item.setCategory(form.getCategory());
		item.setQuantity(form.getQuantity());
		item.setFestivalId(form.getFestivalId());
		item.setUserId(loginUser.getId());

		itemService.update(item);
		return "redirect:/item/list";

	}

	/*@PostMapping("/edit/{id}")と重複しているためコメントアウト
	 @PostMapping("/update")
	public String updateItem(@ModelAttribute Item item) {
		itemService.update(item);
		return "redirect:/item/list";
	}*/

	@GetMapping("/delete/{id}")
	public String deleteItem(@PathVariable("id") Integer id) {
		itemService.delete(id);
		return "redirect:/item/list";

	}
	
}