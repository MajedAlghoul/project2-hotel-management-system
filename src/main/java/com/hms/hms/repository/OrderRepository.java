package com.jed9h3.inventorymanagementsystem.repository;

import com.jed9h3.inventorymanagementsystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    // Additional methods if needed
    //List<Order> findBycustomerID(Long customerId);
}
