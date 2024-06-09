package com.jed9h3.inventorymanagementsystem.service;

import com.jed9h3.inventorymanagementsystem.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto createOrder(OrderDto orderDto);
    void deleteAllOrders();
    OrderDto getOrderById(long id);
    OrderDto updateOrderById(OrderDto orderDto, long id);
    OrderDto partiallyUpdateOrderById(OrderDto orderDto, long id);
    void deleteOrderById(long id);
}
