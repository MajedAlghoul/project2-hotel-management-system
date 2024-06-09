package com.jed9h3.inventorymanagementsystem.repository;

import com.jed9h3.inventorymanagementsystem.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Additional methods if needed
}
