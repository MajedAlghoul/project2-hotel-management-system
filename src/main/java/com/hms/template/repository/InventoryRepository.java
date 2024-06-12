package com.inventorymanagementsystem.repository;

import com.inventorymanagementsystem.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 <p>JpaRepository extends CrudRepository and PagingAndSortingRepository.</p>
 <p>CrudRepository generates a concrete class with the following methods implemented:</p>
 <ul>
 <li>
 <ul>
 <li>createInventory(Inventory inventory)</li>
 <li>getInventory(int inventory_id)</li>
 <li>getAllInventorys()</li>
 <li>updateInventory(Inventory inventory)</li>
 <li>deleteInventory(int inventory_id)</p>
 </ul>
 </li>
 <li>PagingAndSortingRepository provides methods to do pagination and sorting records.</li>
 <li>JpaRepository itself provides some JPA-related methods such as flushing the persistence context and deleting records in a batch.</li>
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
