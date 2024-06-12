package com.inventorymanagementsystem.service;

import com.inventorymanagementsystem.dto.InventoryDto;

public interface InventoryService {
    InventoryDto postInventory(InventoryDto inventory);
    InventoryDto[] getInventories(Integer pageNumber, Integer pageSize);
    InventoryDto replaceInventory(InventoryDto inventory);
    InventoryDto modifyInventory(InventoryDto inventory);
    void deleteInventory(Integer inventory_id);
    InventoryDto getInventoryById(Integer inventory_id);
}
