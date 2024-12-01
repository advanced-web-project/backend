package com.hcmus.demo.security.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * Data Transfer Object (DTO) for authentication requests.
 * This class is used to encapsulate the username and password provided by the user during login.
 */
@Getter
@Setter
public class AuthRequest {
    @NotNull
    @Length(min = 6, max = 50, message = "Username's length must be between 6 and 50")
    private String username; // The username provided by the user

    @NotNull
    @Length(min = 6, max = 50, message = "Password's length must be between 6 and 50")
    private String password; // The password provided by the user
}