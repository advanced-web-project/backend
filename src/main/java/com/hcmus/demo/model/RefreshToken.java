package com.hcmus.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * Entity class representing a refresh token.
 * This class is mapped to the "refresh_tokens" table in the database.
 * It includes fields for the token ID, token string, associated user, and expiry time.
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 256)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User u;

    private Date expiryTime;
}