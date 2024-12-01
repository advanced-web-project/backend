package com.hcmus.demo.token;

import com.hcmus.demo.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing RefreshToken entities.
 * This interface extends JpaRepository to provide CRUD operations for RefreshToken entities.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds all refresh tokens associated with the given username.
     *
     * @param username the username to search for
     * @return a list of refresh tokens associated with the given username
     */
    @Query("select rt from RefreshToken rt where rt.u.username = ?1")
    List<RefreshToken> findByUsername(String username);

    /**
     * Deletes all expired refresh tokens from the database.
     *
     * @return the number of deleted refresh tokens
     */
    @Query("delete from RefreshToken rt where rt.expiryTime <= CURRENT_TIME")
    @Modifying
    int deleteByExpiryTime();
}