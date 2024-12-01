package com.hcmus.demo.exception_handler.exception;

/**
 * Custom exception class for handling scenarios where a refresh token is not found.
 * This class extends Exception to provide a specific exception for cases where a refresh token is missing.
 */
public class RefreshTokenNotFoundException extends Exception {

    /**
     * Constructs a new RefreshTokenNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}