package com.jed9h3.inventorymanagementsystem.service;

import com.jed9h3.inventorymanagementsystem.dto.InventoryDto;

import java.util.List;

public interface InventoryService {
    List<InventoryDto> getAllInventories();
    InventoryDto getInventoryById(long id);
    InventoryDto updateInventoryById(InventoryDto inventoryDto, long id);
    InventoryDto partiallyUpdateInventoryById(InventoryDto inventoryDto, long id);
}
