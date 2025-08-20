package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Item;


@Mapper
public interface ItemMapper{
	
	    List<Item>findAll();
	    List<Item>search(@Param("category") String category,
	    		         @Param("festivalId")Integer festivalId);
        Item findById(long id);
        int insert(Item item);
        int update(Item item);
        int delete(long id);
        
        //ページネーション用
        int countItems();
        List<Item> findItemByPage(@Param("offset")int offset,@Param("limit") int Limit);
        
        //ページネーション検索
        List<Item> searchByPage(String category, Integer festivalId, int offset, int limit);
    	int countSearchItems(String category, Integer festivalId);
   }