/*package com.example.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.domain.Item;
import com.example.app.form.ItemForm;
import com.example.app.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/item") 
@RequiredArgsConstructor
public class ItemApiContoroller {
	
	private final ItemService itemService;

    // 一覧（JSON）
    @GetMapping
    public List<Item> list() {
        return itemService.findAll(); // まずは全件でOK（必要なら検索/ページングは後で）
    }

    // 詳細（JSON）
    @GetMapping("/{id}")
    public ResponseEntity<Item> find(@PathVariable Integer id) {
        Item item = itemService.findById(id);
        return (item == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(item);
    }

    // 新規作成（JSON）
    @PostMapping
    public ResponseEntity<Item> create(@RequestBody ItemForm form) {
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setCategory(form.getCategory());
        item.setQuantity(form.getQuantity());
        item.setFestivalId(form.getFestivalId());
        // ユーザーIDは必要なら後でセッション/トークンから設定
        itemService.insert(item);
        return ResponseEntity.ok(item);
    }

    // 更新（JSON）
    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Integer id, @RequestBody ItemForm form) {
        Item existing = itemService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setItemName(form.getItemName());
        existing.setCategory(form.getCategory());
        existing.setQuantity(form.getQuantity());
        existing.setFestivalId(form.getFestivalId());
        itemService.update(existing);
        return ResponseEntity.ok(existing);
    }

    // 削除（JSON）
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}*/
	
	
	
	


