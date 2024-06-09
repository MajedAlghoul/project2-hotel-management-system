package com.jed9h3.inventorymanagementsystem.repository;

import com.jed9h3.inventorymanagementsystem.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Additional methods if needed
}
