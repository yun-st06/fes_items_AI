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

import com.example.app.domain.Festival;
import com.example.app.domain.Item;
import com.example.app.domain.User;
import com.example.app.form.ItemForm;
import com.example.app.service.FestivalService;
import com.example.app.service.ItemService;
import com.example.app.service.WeatherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	private final FestivalService festivalService;
	private final WeatherService weatherService;

    
	// 持ち物一覧（検索 + ページネーション）
	@GetMapping("/list")
	public String listItems(
	        @RequestParam(required = false) String category,
	        @RequestParam(required = false) Integer festivalId,
	        @RequestParam(defaultValue = "1") int page,
	        Model model,
	        HttpSession session) {

	    // 認可
	    User loginUser = (User) session.getAttribute("loginUser");
	    if (loginUser == null) return "redirect:/login";

	    // ① 入力正規化：空文字は null にする（Thymeleaf がURLに付けないように）
	    if (category != null && category.isBlank()) category = null;

	    // 共通表示
	    model.addAttribute("loginUser", loginUser);
	    model.addAttribute("festivalList", festivalService.findAll());

	    // ② ページング計算
	    final int limit = 10;
	    final int offset = (Math.max(page, 1) - 1) * limit;

	    // ③ データ取得（フィルタ有無で分岐）
	    final List<Item> itemList;
	    final int totalItems;
	    if (category != null || festivalId != null) {
	        itemList   = itemService.searchByPage(category, festivalId, offset, limit);
	        totalItems = itemService.countSearchItems(category, festivalId);
	    } else {
	        itemList   = itemService.findItemByPage(offset, limit);
	        totalItems = itemService.countItems();
	    }

	    final int totalPages = (int) Math.ceil((double) totalItems / limit);

	    // ④ 画面へ
	    model.addAttribute("itemList", itemList);
	    model.addAttribute("currentPage", Math.max(page, 1));
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("category", category);        // ← 空文字→null 済み
	    model.addAttribute("festivalId", festivalId);    // ← null 可
	    model.addAttribute("selectedFestivalId", festivalId);

	    // 天気カード（任意）
	    if (festivalId != null) {
	        Festival fes = festivalService.findById(festivalId.longValue());
	        if (fes != null && fes.getLatitude() != null && fes.getLongitude() != null) {
	            WeatherService.WeatherResult wr =
	                    weatherService.getCurrentByLatLon(fes.getLatitude(), fes.getLongitude());
	            if (wr.hasInfo()) {
	                model.addAttribute("weather", wr.info);
	                String loc = fes.getLocation();
	                model.addAttribute("weatherPlace",
	                        (loc != null && !loc.trim().isEmpty()) ? loc : fes.getFesName());
	            }
	            if (wr.hasError()) model.addAttribute("weatherError", wr.error);
	        } else {
	            model.addAttribute("weatherError", "このフェスの開催地座標が未登録です（latitude/longitude を設定してください）。");
	        }
	    }

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