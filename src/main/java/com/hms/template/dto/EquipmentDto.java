package com.inventorymanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class EquipmentDto {
    private int equipment_id;
    private String name;
    private String model_number;
    private String condition;
    private String description;
}