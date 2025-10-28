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

	//ページネーション
	int countItems();
    List<Item> findItemByPage(int offset, int Limit);
	List<Item> searchByPage(String category, Integer festivalId, int offset, int limit);
    int countSearchItems(String category, Integer festivalId);
		
	//既存	
	void bulkInsert(List<String> itemNames, List<Integer> qty);
	
	//ユーザ/フェス/カテゴリを指定して一括登録
    void bulkInsertWithContext(List<String> itemNames, List<Integer> qty,
                               Integer userId, Integer festivalId, String category);


    //AI自動提案用
    List<Item> suggest(Integer festivalId, Integer days, String lodging, String gender);

}
