package com.hms.template.controller;

import com.inventorymanagementsystem.dto.InventoryDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.inventorymanagementsystem.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Inventory resource.
 */
@RestController
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping(value = "/equipment/{equipment_id}/inventory", produces = "application/json")
    public ResponseEntity<InventoryDto> postInventory(@Validated @PathVariable String equipment_id, @Validated @RequestBody InventoryDto inventory) {
        int equipment_id_int;
        try{
            equipment_id_int = Integer.parseInt(equipment_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Inventory", "inventory_id", equipment_id);
        }
        inventory.setEquipment_id(equipment_id_int);
        return ResponseEntity.ok().body(inventoryService.postInventory(inventory));
    }

    @GetMapping(value = "/equipment/{equipment_id}/inventory", produces = "application/json")
    public ResponseEntity<InventoryDto[]> getInventories(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Inventory", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Inventory", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(inventoryService.getInventories(pageNumberInt, pageSizeInt));
    }

    @PutMapping(value = "/equipment/{equipment_id}/inventory", produces = "application/json")
    public ResponseEntity<InventoryDto> putInventory(@Validated @RequestBody InventoryDto inventory) {
        return ResponseEntity.ok().body(inventoryService.replaceInventory(inventory));
    }

    @PatchMapping(value = "/equipment/{equipment_id}/inventory", produces = "application/json")
    public ResponseEntity<InventoryDto> patchInventory(@Validated @RequestBody InventoryDto inventory) {
        return ResponseEntity.ok().body(inventoryService.modifyInventory(inventory));
    }

    @DeleteMapping(value = "/equipment/{equipment_id}/inventory/{inventory_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteInventory(@Validated @PathVariable String inventory_id) {
        int inventory_id_int;
        try{
            inventory_id_int = Integer.parseInt(inventory_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Inventory", "inventory_id", inventory_id);
        }
        inventoryService.deleteInventory(inventory_id_int);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @GetMapping(value = "/equipment/{equipment_id}/inventory/{inventory_id}", produces = "application/json")
    public ResponseEntity<InventoryDto> getInventory(@Validated @PathVariable String inventory_id) {
        int inventory_id_int;
        try{
            inventory_id_int = Integer.parseInt(inventory_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Inventory", "inventory_id", inventory_id);
        }
        return ResponseEntity.ok().body(inventoryService.getInventoryById(inventory_id_int));
    }
}