package com.hms.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

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
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primary key
    private long id;
    @Column(nullable = false)
    private String checkingStatus;
    @Column(nullable = false)
    private String checkInData;
    @Column(nullable = false)
    private String checkOutData;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"))
    private Set<Room> room;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Bill bill;
}