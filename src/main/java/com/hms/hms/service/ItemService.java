package com.jed9h3.inventorymanagementsystem.service;

import com.jed9h3.inventorymanagementsystem.dto.ItemDto;
import com.jed9h3.inventorymanagementsystem.entity.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllItems();
    ItemDto createItem(ItemDto itemDto);
    void deleteAllItems();
    ItemDto getItemById(long id);
    Item getRawItemById(long id);
    ItemDto updateItemById(ItemDto itemDto, long id);
    ItemDto partiallyUpdateItemById(ItemDto itemDto, long id);
    void deleteItemById(long id);
    ItemDto convertToDto(Item item);
    Item convertToEntity(ItemDto itemDto);
}
