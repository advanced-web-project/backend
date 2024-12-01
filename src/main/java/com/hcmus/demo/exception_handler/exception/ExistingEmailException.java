package com.hcmus.demo.exception_handler.exception;

/**
 * Custom exception class for handling scenarios where an email already exists in the system.
 * This class extends Exception to provide a specific exception for cases where an email is found to be already in use.
 */
public class ExistingEmailException extends Exception {

    /**
     * Constructs a new ExistingEmailException with the specified detail message.
     *
     * @param message the detail message
     */
    public ExistingEmailException(String message) {
        super(message);
    }
}