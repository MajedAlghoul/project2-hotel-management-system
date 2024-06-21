package com.hms.dto;

import com.hms.model.Reservation;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class BillDto {
    private Long id;
    private Long reservation;
    private Long invoice_total;
    private Long paid;
    private Long due;
}