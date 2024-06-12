package com.inventorymanagementsystem.repository;

import com.inventorymanagementsystem.model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 <p>JpaRepository extends CrudRepository and PagingAndSortingRepository.</p>
 <p>CrudRepository generates a concrete class with the following methods implemented:</p>
 <ul>
 <li>
 <ul>
 <li>createLease(Lease lease)</li>
 <li>getLease(int lease_id)</li>
 <li>getAllLeases()</li>
 <li>updateLease(Lease lease)</li>
 <li>deleteLease(int lease_id)</p>
 </ul>
 </li>
 <li>PagingAndSortingRepository provides methods to do pagination and sorting records.</li>
 <li>JpaRepository itself provides some JPA-related methods such as flushing the persistence context and deleting records in a batch.</li>
 */
@Repository
public interface LeaseRepository extends JpaRepository<Lease, Integer> {
}
