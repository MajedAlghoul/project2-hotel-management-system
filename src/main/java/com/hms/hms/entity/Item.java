package com.jed9h3.inventorymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;
    private String itemName;
    private BigDecimal price;
    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private Inventory inventory;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Order> orders;
}
