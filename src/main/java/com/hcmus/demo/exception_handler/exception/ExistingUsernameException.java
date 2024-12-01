package com.hcmus.demo.exception_handler.exception;

/**
 * Custom exception class for handling scenarios where a username already exists in the system.
 * This class extends Exception to provide a specific exception for cases where a username is found to be already in use.
 */
public class ExistingUsernameException extends Exception {

    /**
     * Constructs a new ExistingUsernameException with the specified detail message.
     *
     * @param message the detail message
     */
    public ExistingUsernameException(String message) {
        super(message);
    }
}