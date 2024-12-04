package com.hcmus.demo.user;

import lombok.*;

/**
 * Data Transfer Object (DTO) for User responses.
 * This class is used to transfer user data in responses.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id; // The unique identifier of the user
    private String username; // The username of the user
    private String email; // The email address of the user
    private String profile; // The profile information of the user
}