package com.brodygaudel.accountservice.exception;

/**
 * Exception thrown to indicate that a customer already has an existing account.
 * This exception is typically used to handle cases where a new account creation
 * is attempted for a customer who already has an active account.
 * @author Brody Gaudel
 */
public class CustomerAlreadyHaveAccountException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CustomerAlreadyHaveAccountException(String message) {
        super(message);
    }
}
