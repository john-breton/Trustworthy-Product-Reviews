package com.productreviews.Repositories;

import com.productreviews.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * defines common persistence operations (including CRUD) and
 * the implementation will be generated at runtime by Spring Data JPA.
 */
public interface UserRepository extends JpaRepository<User, Long>{

//    @Query("SELECT u FROM User u WHERE u.username = ?1") // might need this when connecting to postgres
    User findByUsername(String username);
}
