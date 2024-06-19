package com.hms.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class RoomDto {
    private Long RoomId;
    private String details;
    private Long price;
    private String facilities;
    private Integer capacity;
    private Long size;
    private String features;
    private String type;
    private String availability;
    private String status;
}