package com.hcmus.demo.user;

import com.hcmus.demo.exception_handler.exception.ExistingEmailException;
import com.hcmus.demo.exception_handler.exception.ExistingUsernameException;
import com.hcmus.demo.model.User;
import com.hcmus.demo.security.jwt.JwtValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for user-related operations.
 * This class provides endpoints for user registration, checking unique email and username, and finding a user by token.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserApi {
    private final UserService userService;
    private final ModelMapper modelMapper;

    /**
     * Endpoint for user registration.
     *
     * @param userDto the user data transfer object
     * @return the response entity with the registered user
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO userDto) throws ExistingUsernameException, ExistingEmailException {
        User user = dtoToEntity(userDto);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(addLinks2Item(entityToDto(savedUser)));
    }

    /**
     * Endpoint to check if an email is unique.
     *
     * @param email the email to check
     * @return the response entity with the result
     */
    @GetMapping("/check-unique-email/{email}")
    public ResponseEntity<?> checkUniqueEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkUniqueEmail(email));
    }

    /**
     * Endpoint to check if a username is unique.
     *
     * @param username the username to check
     * @return the response entity with the result
     */
    @GetMapping("/check-unique-username/{username}")
    public ResponseEntity<?> checkUniqueUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.checkUniqueUsername(username));
    }

    /**
     * Endpoint to find a user by token.
     *
     * @return the response entity with the user
     * @throws JwtValidationException if the token is invalid
     */
    @GetMapping("/profile")
    public ResponseEntity<?> findUserByToken(HttpServletRequest request) throws JwtValidationException, ExistingUsernameException, ExistingEmailException {
        String token = getBearerToken(request);
        User user = userService.getUserByToken(token);
        return ResponseEntity.ok(addLinks2Item(entityToDto(user)));
    }

    /**
     * Converts a UserDTO to a User entity.
     *
     * @param userDto the user data transfer object
     * @return the user entity
     */
    public User dtoToEntity(UserRequestDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the user entity
     * @return the user data transfer object
     */
    public UserResponseDTO entityToDto(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }

    /**
     * Adds HATEOAS links to a UserDTO.
     *
     * @param userDto the user data transfer object
     * @return the user data transfer object with links
     */
    public UserResponseDTO addLinks2Item(UserResponseDTO userDto) throws ExistingUsernameException, ExistingEmailException {
        userDto.add(linkTo(methodOn(UserApi.class).register(null)).withSelfRel());
        userDto.add(linkTo(methodOn(UserApi.class).checkUniqueEmail(null)).withRel("Check unique email"));
        userDto.add(linkTo(methodOn(UserApi.class).checkUniqueUsername(null)).withRel("Check unique username"));
        return userDto;
    }

    /**
     * Extracts the Bearer token from the Authorization header.
     *
     * @param request the HTTP request
     * @return the Bearer token, or null if not present
     */
    private String getBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        String[] array = header.split(" ");
        if (array.length == 2) return array[1];
        return null;
    }

}