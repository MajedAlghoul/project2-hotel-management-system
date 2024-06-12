package com.inventorymanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class InventoryDto {
    private int inventory_id;
    private int equipment_id;
    private int branch_id;
    private String serial_number;
}