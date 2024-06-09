package com.jed9h3.inventorymanagementsystem.controller;

import com.jed9h3.inventorymanagementsystem.dto.OrderDto;
import com.jed9h3.inventorymanagementsystem.exception.BadRequestException;
import com.jed9h3.inventorymanagementsystem.service.OrderService;
//import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        if (orderDto.getOrderId() != null) {
            throw new BadRequestException("Bad Request: ID provided");
        }
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllOrders() {
        orderService.deleteAllOrders();
        return new ResponseEntity<>("All orders has been deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrderById(@Valid @RequestBody OrderDto orderDto
            , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(orderService.updateOrderById(orderDto, id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDto> partiallyUpdateOrderById(@Valid @RequestBody OrderDto orderDto
            , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(orderService.partiallyUpdateOrderById(orderDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable(name = "id") long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity<>("Order number "+id+" has been Deleted successfully.", HttpStatus.OK);
    }
}
