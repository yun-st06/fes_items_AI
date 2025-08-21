package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.app.domain.Item;
import com.example.app.mapper.ItemMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    
	private final ItemMapper itemMapper;
	
	@Override
	public List<Item> findAll(){
		return itemMapper.findAll();
	}
	
	@Override
	public List<Item> search(String category,Integer festivalId){
		return itemMapper.search(category,festivalId);
		
	}
	
	@Override
	public Item findById(long id) {
		return itemMapper.findById(id);
	}
	
	@Override
	public int insert(Item item) {
		return itemMapper.insert(item);
		
	}
	
	@Override
	public int update(Item item) {
		return itemMapper.update(item);
		
	}
	
	@Override
	public int delete(long id) {
		return itemMapper.delete(id);
		
	}
	
	//ページネーション用:全件
	@Override
	public int countItems() {
		return itemMapper.countItems();
		
	}
	
	@Override
	public List<Item>findItemByPage(int offset,int limit){
		return itemMapper.findItemByPage(offset, limit);
		
	}
	
	//ページネーション用:検索条件付き
	@Override
	public List<Item> searchByPage(String category, Integer festivalId, int offset, int limit) {
		return itemMapper.searchByPage(category, festivalId, offset, limit);
	}

	@Override
	public int countSearchItems(String category, Integer festivalId) {
		return itemMapper.countSearchItems(category, festivalId);
	}
}
	

