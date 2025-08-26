package com.example.app.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


	//持ち物一覧②検索条件でページネーション付けたバージョン
	@GetMapping("/list")
	public String listItems(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Integer festivalId,
			@RequestParam(defaultValue = "1") int page,
			Model model,
			HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// 共通情報
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("festivalList", festivalService.findAll());

		int limit = 10;
		int offset = (page - 1) * limit;

		List<Item> itemList;
		int totalItems;

		if ((category != null && !category.isEmpty()) || (festivalId != null)) {
			// 絞り込みあり
			itemList = itemService.searchByPage(category, festivalId, offset, limit);
			totalItems = itemService.countSearchItems(category, festivalId);
		} else {
			// 絞り込みなし
			itemList = itemService.findItemByPage(offset, limit);
			totalItems = itemService.countItems();
		}

		int totalPages = (int) Math.ceil((double) totalItems / limit);

		model.addAttribute("itemList", itemList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("category", category); // フォーム再表示用
		model.addAttribute("festivalId", festivalId); // フォーム再表示用

		return "item_list";
	}

	//持ち物新規登録
	@GetMapping("/new")
	public String showNewForm(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("itemForm", new ItemForm());
		model.addAttribute("festivalList", festivalService.findAll());

		return "item_new";
	}

	//持ち物新規登録のエラー表示
	@PostMapping("/new")
	public String register(@Valid ItemForm form,
			BindingResult result,
			Model model,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			model.addAttribute("festivalList", festivalService.findAll());
			return "item_new";
		}

		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login"; // ログイン画面に飛ばす
		}

		//DTOからDBへ登録処理
		Item item = new Item();
		item.setItemName(form.getItemName());
		item.setCategory(form.getCategory());
		item.setQuantity(form.getQuantity());
		item.setFestivalId(form.getFestivalId());
		item.setUserId(loginUser.getId());

		itemService.insert(item);

		redirectAttributes.addFlashAttribute("message", "登録しました!");
		return "redirect:/item/list";
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
	public String update(@Valid ItemForm form,
			BindingResult result,
			Model model,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

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

		redirectAttributes.addFlashAttribute("message", "更新しました！");

		return "redirect:/item/list";

	}

	@GetMapping("/delete/{id}")
	public String deleteItem(@PathVariable("id") long id,
			RedirectAttributes redirectAttributes) {
		itemService.delete(id);

		redirectAttributes.addFlashAttribute("message", "削除しました!");

		return "redirect:/item/list";

	}

}