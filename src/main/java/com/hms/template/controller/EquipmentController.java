package com.hms.template.controller;

import com.inventorymanagementsystem.dto.EquipmentDto;
import com.hms.exception.IllegalInputException;
import com.hms.responsebody.StandardMessageBody;
import com.inventorymanagementsystem.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Contains endpoints related to the Equipment resource.
 */
@RestController
public class EquipmentController {
    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping(value = "/equipment", produces = "application/json")
    public ResponseEntity<EquipmentDto> postEquipment(@Validated @RequestBody EquipmentDto equipment) {
        return ResponseEntity.ok().body(equipmentService.postEquipment(equipment));
    }

    @GetMapping(value = "/equipment", produces = "application/json")
    public ResponseEntity<EquipmentDto[]> getEquipment(@Validated @RequestParam String pageNumber, @RequestParam String pageSize) {
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.parseInt(pageNumber);
            pageSizeInt = Integer.parseInt(pageSize);
            if(pageNumberInt < 0 || pageNumberInt > pageSizeInt || pageSizeInt < 1)
                throw new IllegalInputException("Equipment", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Equipment", "pageNumber and or pageSize", pageNumber + " and or " + pageSize);
        }
        return ResponseEntity.ok().body(equipmentService.getEquipments(pageNumberInt, pageSizeInt));
    }

    @PutMapping(value = "/equipment", produces = "application/json")
    public ResponseEntity<EquipmentDto> putEquipment(@Validated @RequestBody EquipmentDto equipment) {
        return ResponseEntity.ok().body(equipmentService.replaceEquipment(equipment));
    }

    @PatchMapping(value = "/equipment", produces = "application/json")
    public ResponseEntity<EquipmentDto> patchEquipment(@Validated @RequestBody EquipmentDto equipment) {
        return ResponseEntity.ok().body(equipmentService.modifyEquipment(equipment));
    }

    @DeleteMapping(value = "/equipment/{equipment_id}", produces = "application/json")
    public ResponseEntity<HashMap<String, String>> deleteEquipment(@Validated @PathVariable String equipment_id) {
        int id;
        try{
            id = Integer.parseInt(equipment_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Equipment", "equipment_id", equipment_id);
        }
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok().body(new StandardMessageBody("Deleted successfully.").getContent());
    }

    @GetMapping(value = "/equipment/{equipment_id}", produces = "application/json")
    public ResponseEntity<EquipmentDto> getEquipment(@Validated @PathVariable String equipment_id) {
        int id;
        try{
            id = Integer.parseInt(equipment_id);
        } catch(NumberFormatException e){
            throw new IllegalInputException("Equipment", "equipment_id", equipment_id);
        }
        return ResponseEntity.ok().body(equipmentService.getEquipmentById(id));
    }
}