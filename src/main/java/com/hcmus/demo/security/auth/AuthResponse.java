package com.hcmus.demo.security.auth;

import com.hcmus.demo.user.UserResponseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for authentication responses.
 * This class is used to encapsulate the access token generated after successful authentication.
 */
@Getter
@Setter
public class AuthResponse {
    private String accessToken; // The access token generated after successful authentication
    private String refreshToken; // The refresh token generated after successful authentication
    private UserResponseDTO user; // The user information associated with the access token
}