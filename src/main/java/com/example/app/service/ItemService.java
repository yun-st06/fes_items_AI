package com.example.app.service;

import java.util.List;

import com.example.app.domain.Item;

public interface ItemService {
      List<Item> findAll();
      List<Item> search(String category,Integer festivalId);
      Item findById(long id);
      void insert (Item item);
      void update(Item item);
      void delete(long id);
   }
