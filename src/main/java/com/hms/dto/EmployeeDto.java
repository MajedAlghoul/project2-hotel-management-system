package com.hms.dto;

import lombok.Builder;
import lombok.Data;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class EmployeeDto {
    private int employee_email;
    private int name;
    private int password_hash;
}