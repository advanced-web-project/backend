package com.hcmus.demo.user;

import com.hcmus.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity.
 * This interface extends JpaRepository to provide CRUD operations for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user
     * @return the user with the given username, or null if no user found
     */
    User findByUsername(String username);

    /**
     * Finds a user by email.
     *
     * @param email the email of the user
     * @return the user with the given email, or null if no user found
     */
    User findByEmail(String email);
}