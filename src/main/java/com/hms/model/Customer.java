package com.hms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
    <p>This class represents an Employee entity</p>
    <p>An entity represents a database table to JPA</p>
 */

@Builder // Generates a static Builder-Design-Pattern builder method
@AllArgsConstructor // Generates a constructor with all fields as formal parameters
@NoArgsConstructor // Generates a constructor with no formal parameters
@Data // Generates Getters, Setters, toString, hashCode, & equals methods for non-transient fields
@Entity // Specifies to the JPA that this class is an entity to be mapped to a database table
@Table(name = "customer", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_email"})
}) // Specifies to the JPA the table details to persist the entity in the database
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String customerEmail;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> role;

}
