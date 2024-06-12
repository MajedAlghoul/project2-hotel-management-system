package com.inventorymanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class LeaseDto {
    private int lease_id;
    private int employee_id;
    private int inventory_id;
    private String start;
    private String end;
}