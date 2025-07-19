package org.airtribe.TaskMaster.repository;
import java.util.List;

import org.airtribe.TaskMaster.entity.Task;

import org.airtribe.TaskMaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long>{
    
    List<Task> findByAssignedTo(User user);

    List<Task> findByAssignedToAndStatus(User user, Task.Status status);

    @Query("SELECT t FROM Task t WHERE (LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND t.assignedTo = :user")
    List<Task> searchUserTasks(@Param("query") String query, @Param("user") User user);

}
