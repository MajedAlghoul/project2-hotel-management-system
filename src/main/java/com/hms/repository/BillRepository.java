package com.hms.repository;

import com.hms.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 <p>JpaRepository extends CrudRepository and PagingAndSortingRepository.</p>
 <p>CrudRepository generates a concrete class with the following methods implemented:</p>
 <ul>
 <li>
 <ul>
 <li>createBill(Bill bill)</li>
 <li>getBill(int bill_id)</li>
 <li>getAllBills()</li>
 <li>updateBill(Bill bill)</li>
 <li>deleteBill(int bill_id)</p>
 </ul>
 </li>
 <li>PagingAndSortingRepository provides methods to do pagination and sorting records.</li>
 <li>JpaRepository itself provides some JPA-related methods such as flushing the persistence context and deleting records in a batch.</li>
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
}
