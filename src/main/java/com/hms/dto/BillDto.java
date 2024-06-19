package com.hms.dto;

import com.hms.model.Reservation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Builder // Generates a static Builder-Design-Pattern builder method
public class BillDto {
    private Long id;
    private Set<Reservation> reservation;
    private Long invoice_total;
    private Long paid;
    private Long due;
}