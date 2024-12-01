package com.hcmus.demo.exception_handler.exception;

/**
 * Custom exception class for handling scenarios where a refresh token has expired.
 * This class extends Exception to provide a specific exception for cases where a refresh token is found to be expired.
 */
public class RefreshTokenExpireException extends Exception {

    /**
     * Constructs a new RefreshTokenExpireException with the specified detail message.
     *
     * @param message the detail message
     */
    public RefreshTokenExpireException(String message) {
        super(message);
    }
}