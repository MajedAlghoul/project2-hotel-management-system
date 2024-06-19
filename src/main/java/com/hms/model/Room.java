package com.hms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 <p>This class represents an Task entity</p>
 <p>An entity represents a database table to JPA</p>
 */

@Builder // Generates a static Builder-Design-Pattern builder method
@AllArgsConstructor // Generates a constructor with all fields as formal parameters
@NoArgsConstructor // Generates a constructor with no formal parameters
@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Entity // Specifies to the JPA that this class is an entity to be mapped to a database table
@Table // Specifies to the JPA the table details to persist the entity in the database
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key
    private long id;
    @Column(nullable = false)
    private String details;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    private String facilities;
    @Column(nullable = false)
    private Integer capacity;
    @Column(nullable = false)
    private Long size;
    @Column(nullable = false)
    private String features;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String availability;
    @Column(nullable = false)
    private String status;
}
