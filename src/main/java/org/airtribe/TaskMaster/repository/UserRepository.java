package org.airtribe.TaskMaster.repository;

import org.airtribe.TaskMaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findUserById(Long id);
 

}
