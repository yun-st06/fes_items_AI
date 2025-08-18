package com.example.app.controller;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Item;
import com.example.app.domain.User;
import com.example.app.service.FestivalService;
import com.example.app.service.ItemService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController{
	
	private final ItemService itemService;
	private final FestivalService festivalService;
	
	
@GetMapping("/list")
	public String listItems(@RequestParam(required = false)String category,
			                @RequestParam(required = false)Integer festivalId,
                            Model model, HttpSession session) {
	    User loginUser = (User) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return "redirect:/login";
	    }

	    model.addAttribute("loginUser", loginUser);
	    model.addAttribute("festivalList",festivalService.findAll());
	    
	    
	    if((category != null && !category.isEmpty()) ||
	    	(festivalId !=null)) {
	    	    model.addAttribute("itemList",itemService.search(category,festivalId));
	    }else {
	    	model.addAttribute("itemList",itemService.findAll());
	    	
	    }
	    
        return "item_list";
	}
	
	
	@GetMapping("/new")
	public String showNewForm(Model model) {
		model.addAttribute("item",new Item());
		model.addAttribute("festivalList",festivalService.findAll());
		return "item_new";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id")Integer id, Model model) {
		   Item item =itemService.findById(id);
		   model.addAttribute("item", item);
		   model.addAttribute("festivalList",festivalService.findAll());
		   return "item_edit";
    }
	
	@PostMapping("/update")
	public String updateItem(@ModelAttribute Item item) {
		itemService.update(item);
		return "redirect:/item/list";
	}
	
	@GetMapping("/delete/{id}")
	 public String deleteItem(@PathVariable("id")Integer id){
		itemService.delete(id);
		return "redirect:/item/list";
		
	}
	
	
	
	
	
}