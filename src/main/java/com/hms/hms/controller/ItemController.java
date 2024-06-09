package com.jed9h3.inventorymanagementsystem.controller;

import com.jed9h3.inventorymanagementsystem.dto.ItemDto;
import com.jed9h3.inventorymanagementsystem.exception.BadRequestException;
//import org.springframework.http.ResponseEntity;
import com.jed9h3.inventorymanagementsystem.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        if (itemDto.getItemId() != null) {
            throw new BadRequestException("Bad Request: ID provided");
        }
        return new ResponseEntity<>(itemService.createItem(itemDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllItems() {
        itemService.deleteAllItems();
        return new ResponseEntity<>("All items has been deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(
            @PathVariable(name = "id") long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItemById(@Valid @RequestBody ItemDto itemDto
            , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(itemService.updateItemById(itemDto, id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> partiallyUpdateItemById(@Valid @RequestBody ItemDto itemDto
            , @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(itemService.partiallyUpdateItemById(itemDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemById(@PathVariable(name = "id") long id) {
        itemService.deleteItemById(id);
        return new ResponseEntity<>("Item number "+id+" has been Deleted successfully.", HttpStatus.OK);
    }
}
