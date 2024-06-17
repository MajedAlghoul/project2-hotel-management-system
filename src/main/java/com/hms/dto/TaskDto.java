package com.hms.dto;

import lombok.Builder;
import lombok.Data;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class TaskDto {
    private String id;
    private String name;
    private String description;
    private String assigned_to;
}