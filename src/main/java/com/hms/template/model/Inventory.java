package com.inventorymanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    <p>This class represents an Inventory entity</p>
    <p>An entity represents a database table to JPA</p>
 */

@Builder // Generates a static Builder-Design-Pattern builder method
@AllArgsConstructor // Generates a constructor with all fields as formal parameters
@NoArgsConstructor // Generates a constructor with no formal parameters
@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Entity // Specifies to the JPA that this class is an entity to be mapped to a database table
@Table // Specifies to the JPA the table details to persist the entity in the database
public class Inventory {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented by the persistence provider
    private int inventory_id;
    @Column(nullable = false)
    private int equipment_id;
    @Column(nullable = false)
    private int branch_id;
    @Column(nullable = false)
    private String serial_number;
}