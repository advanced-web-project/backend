package com.hcmus.demo.exception_handler.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom exception class for handling user not found scenarios.
 * This class extends UsernameNotFoundException to provide a specific exception
 * for cases where a user is not found in the system.
 */
public class UserNotFoundException extends UsernameNotFoundException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param msg the detail message
     */
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
