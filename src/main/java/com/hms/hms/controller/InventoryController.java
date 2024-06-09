package com.jed9h3.inventorymanagementsystem.controller;

import com.jed9h3.inventorymanagementsystem.dto.InventoryDto;
import com.jed9h3.inventorymanagementsystem.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryDto> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDto> getInventoryById(
            @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDto> updateInventoryById(@Valid @RequestBody InventoryDto inventoryDto
            , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(inventoryService.updateInventoryById(inventoryDto, id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InventoryDto> partiallyUpdateInventoryById(@Valid @RequestBody InventoryDto inventoryDto
            , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(inventoryService.partiallyUpdateInventoryById(inventoryDto, id), HttpStatus.OK);
    }
}
