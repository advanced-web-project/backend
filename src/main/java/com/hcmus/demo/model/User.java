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

    @Column(nullable = false)
    private String firstName; // First name of the user

    @Column(nullable = false)
    private String lastName; // Last name of the user

    @Column(nullable = false)
    private String address; // Address of the user

    @Column(nullable = false)
    private Date createdAt; // Date when the user was created

    /**
     * Returns the full name of the user by concatenating first name and last name.
     *
     * @return the full name of the user
     */
    public String fullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
