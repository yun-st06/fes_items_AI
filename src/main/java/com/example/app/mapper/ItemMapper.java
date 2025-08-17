package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Item;


@Mapper
public interface ItemMapper{
	
	    List<Item>findAll();
	    List<Item>search(String Kw,String cat);
        Item findById(long id);
        int insert(Item item);
        int update(Item item);
        int delete(long id);
        
   }