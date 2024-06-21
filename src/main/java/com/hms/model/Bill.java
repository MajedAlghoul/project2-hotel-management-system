package com.hms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
    <p>This class represents an Bill entity</p>
    <p>An entity represents a database table to JPA</p>
 */

@Builder // Generates a static Builder-Design-Pattern builder method
@AllArgsConstructor // Generates a constructor with all fields as formal parameters
@NoArgsConstructor // Generates a constructor with no formal parameters
@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Entity // Specifies to the JPA that this class is an entity to be mapped to a database table
//@Table(name = "bill")// Specifies to the JPA the table details to persist the entity in the database
public class Bill {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    private Long invoice_total;
    private Long paid;
    private Long due;
}
