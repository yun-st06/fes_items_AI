package com.example.app.service;

import java.util.List;

import com.example.app.domain.Item;

public interface ItemService {
	List<Item> findAll();

	List<Item> search(String category, Integer festivalId);

	Item findById(long id);

	int insert(Item item);

	int update(Item item);

	int delete(long id);

	//ページネーション用
	int countItems();

	List<Item> findItemByPage(int offset, int Limit);
	
	// 検索 + ページネーション用
		List<Item> searchByPage(String category, Integer festivalId, int offset, int limit);

		int countSearchItems(String category, Integer festivalId);
	

}
