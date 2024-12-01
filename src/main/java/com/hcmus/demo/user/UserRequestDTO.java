package com.hcmus.demo.user;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.CollectionModel;

/**
 * Data Transfer Object (DTO) for User.
 * This class is used to transfer user data between processes.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO extends CollectionModel<UserRequestDTO> {

    @NotNull(message = "Username is required.")
    @NotBlank(message = "Username is not empty.")
    @Size(min = 6, max = 50, message = "Username must be greater than 5 and not exceed 50 characters.")
    private String username;

    @NotNull(message = "Email is required.")
    @NotBlank(message = "Email is not empty.")
    @Email(message = "Email should be valid.")
    @Size(max = 100, message = "Email must not exceed 100 characters.")
    private String email;

    @NotNull(message = "Password is required.")
    @NotBlank(message = "Password is not empty.")
    @Size(min = 6, message = "Password must be at least 6 characters.")
    private String password;

    @NotNull(message = "First name is required.")
    @NotBlank(message = "First name is not empty.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    private String firstName;

    @NotNull(message = "Last name is required.")
    @NotBlank(message = "Last name is not empty.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    private String lastName;

    @Size(max = 255, message = "Address must not exceed 255 characters.")
    @NotBlank(message = "Address is not empty.")
    private String address;
}