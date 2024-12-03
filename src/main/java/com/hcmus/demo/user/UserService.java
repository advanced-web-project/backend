package com.hcmus.demo.user;

import com.hcmus.demo.exception_handler.exception.ExistingEmailException;
import com.hcmus.demo.exception_handler.exception.ExistingUsernameException;
import com.hcmus.demo.exception_handler.exception.UserNotFoundException;
import com.hcmus.demo.model.User;
import com.hcmus.demo.security.jwt.JwtUtility;
import com.hcmus.demo.security.jwt.JwtValidationException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service class for user-related operations.
 * This class provides methods for saving users, encoding passwords, checking unique email and username, and retrieving users by token.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND_MSG = "User with the given token not found";
    private static final String EMAIL_EXISTS_MSG = "Email already exists";
    private static final String USERNAME_EXISTS_MSG = "Username already exists";

    /**
     * Saves a user to the repository.
     * Encodes the user's password and sets the creation date before saving.
     *
     * @param user the user to save
     * @return the saved user
     */
    public User saveUser(User user) throws ExistingEmailException, ExistingUsernameException {

        if (!checkUniqueEmail(user.getEmail())) {
            throw new ExistingEmailException(EMAIL_EXISTS_MSG);
        }

        if (!checkUniqueUsername(user.getUsername())) {
            throw new ExistingUsernameException(USERNAME_EXISTS_MSG);
        }

        encodePassword(user);
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }

    /**
     * Encodes the user's password using the password encoder.
     *
     * @param user the user whose password is to be encoded
     */
    public void encodePassword(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
    }

    /**
     * Checks if an email is unique.
     *
     * @param email the email to check
     * @return true if the email is unique, false otherwise
     */
    public boolean checkUniqueEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user == null;
    }

    /**
     * Checks if a username is unique.
     *
     * @param username the username to check
     * @return true if the username is unique, false otherwise
     */
    public boolean checkUniqueUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user == null;
    }

    /**
     * Retrieves a user by token.
     * Validates the token and extracts user details from it.
     *
     * @param token the token to validate and extract user details from
     * @return the user associated with the token
     * @throws JwtValidationException if the token is invalid
     */
    public User getUserByToken(String token) throws JwtValidationException {
        Claims claims = jwtUtility.validateAccessToken(token);
        String subject = (String) claims.get(Claims.SUBJECT);
        String[] array = subject.split(",");

        Long userId = Long.valueOf(array[0]);
        String username = array[1];

        User user = userRepository.findByUsername(username);
        if (user == null || user.getId() != userId) {
            throw new UserNotFoundException(USER_NOT_FOUND_MSG);
        }
        return user;
    }
}