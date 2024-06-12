package com.inventorymanagementsystem.repository;

import com.inventorymanagementsystem.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 <p>JpaRepository extends CrudRepository and PagingAndSortingRepository.</p>
 <p>CrudRepository generates a concrete class with the following methods implemented:</p>
 <ul>
 <li>
 <ul>
 <li>createEquipment(Equipment equipment)</li>
 <li>getEquipment(int equipment_id)</li>
 <li>getAllEquipments()</li>
 <li>updateEquipment(Equipment equipment)</li>
 <li>deleteEquipment(int equipment_id)</p>
 </ul>
 </li>
 <li>PagingAndSortingRepository provides methods to do pagination and sorting records.</li>
 <li>JpaRepository itself provides some JPA-related methods such as flushing the persistence context and deleting records in a batch.</li>
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
}
