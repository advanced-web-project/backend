package com.hcmus.demo.security.jwt;

/**
 * Custom exception class for handling JWT validation errors.
 * This exception is thrown when a JWT token is invalid or expired.
 */
public class JwtValidationException extends Exception {
    /**
     * Constructs a new JwtValidationException with the specified detail message and cause.
     *
     * @param message   the detail message
     * @param throwable the cause of the exception
     */
    public JwtValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}