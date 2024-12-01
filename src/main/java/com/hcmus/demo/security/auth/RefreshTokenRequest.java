package com.hcmus.demo.security.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for refresh token requests.
 * This class is used to encapsulate the data required for requesting a new access token using a refresh token.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private String username; // The username associated with the refresh token
    private String refreshToken; // The refresh token used to request a new access token
}