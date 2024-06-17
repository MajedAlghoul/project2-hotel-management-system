package com.hms.repository;

import com.hms.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 <p>JpaRepository extends CrudRepository and PagingAndSortingRepository.</p>
 <p>CrudRepository generates a concrete class with the following methods implemented:</p>
 <ul>
 <li>
 <ul>
 <li>createTask(Task task)</li>
 <li>getTask(int task_id)</li>
 <li>getAllTasks()</li>
 <li>updateTask(Task task)</li>
 <li>deleteTask(int task_id)</p>
 </ul>
 </li>
 <li>PagingAndSortingRepository provides methods to do pagination and sorting records.</li>
 <li>JpaRepository itself provides some JPA-related methods such as flushing the persistence context and deleting records in a batch.</li>
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
}
