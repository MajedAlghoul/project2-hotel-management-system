package com.hms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

/**
 <p>This class represents an Reservation entity</p>
 <p>An entity represents a database table to JPA</p>
 */

@Builder // Generates a static Builder-Design-Pattern builder method
@AllArgsConstructor // Generates a constructor with all fields as formal parameters
@NoArgsConstructor // Generates a constructor with no formal parameters
@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Entity // Specifies to the JPA that this class is an entity to be mapped to a database table
//@Table(name = "reservation")// Specifies to the JPA the table details to persist the entity in the database
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    private String customerEmail;
    private String checkingStatus;
    private Long billId;
    private Date checkin;
    private Date checkout;
}
