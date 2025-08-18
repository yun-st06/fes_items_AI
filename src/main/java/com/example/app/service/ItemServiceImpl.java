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
	public void insert(Item item) {
		itemMapper.insert(item);
		
	}
	
	@Override
	public void update(Item item) {
		itemMapper.update(item);
		
	}
	
	@Override
	public void delete(long id) {
		itemMapper.delete(id);
		
	}
	
	
	
	
}
