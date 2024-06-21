package com.hms.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class ReservationDto {
    private Long id;
    private String customerEmail;
    private String checkingStatus;
    private String checkin;
    private String checkout;
    private Long room;
}