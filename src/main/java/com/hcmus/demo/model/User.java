package com.hcmus.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Entity class representing a User in the system.
 * This class is mapped to the "User" table in the database.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the user

    @Column(unique = true, nullable = false)
    private String username; // Username of the user, must be unique

    @Column(unique = true, nullable = false)
    private String email; // Email of the user, must be unique

    @Column(nullable = false)
    private String password; // Password of the user
    @Column
    private String profile;
    @Column(nullable = false)
    private Date createdAt; // Date when the user was created
}
