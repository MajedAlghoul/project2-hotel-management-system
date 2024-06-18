package com.hms.dto;

import lombok.Builder;
import lombok.Data;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class CustomerDto {
    private String customerEmail;
    private String name;
    private String passwordHash;
}