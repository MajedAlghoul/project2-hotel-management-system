package com.hms.repository;

import com.hms.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 <p>JpaRepository extends CrudRepository and PagingAndSortingRepository.</p>
 <p>CrudRepository generates a concrete class with the following methods implemented:</p>
 <ul>
 <li>
 <ul>
 <li>createReservation(Reservation reservation)</li>
 <li>getReservation(int reservation_id)</li>
 <li>getAllReservations()</li>
 <li>updateReservation(Reservation reservation)</li>
 <li>deleteReservation(int reservation_id)</p>
 </ul>
 </li>
 <li>PagingAndSortingRepository provides methods to do pagination and sorting records.</li>
 <li>JpaRepository itself provides some JPA-related methods such as flushing the persistence context and deleting records in a batch.</li>
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
