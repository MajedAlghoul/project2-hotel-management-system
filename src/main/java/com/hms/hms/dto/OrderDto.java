package com.jed9h3.inventorymanagementsystem.dto;

import lombok.Data;

@Data
public class OrderDto {
    private Long orderId;
    private Long itemId;
    private Long customerId;
    private Long orderedQuantity;
}
