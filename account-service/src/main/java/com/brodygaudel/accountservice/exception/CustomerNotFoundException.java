package com.brodygaudel.accountservice.exception;

/**
 * Exception thrown to indicate that a customer was not found.
 *
 * <p>This exception is typically thrown when an attempt is made to access or manipulate a
 * customer entity that does not exist.</p>
 *
 * <p>Author: Brody Gaudel</p>
 */
public class CustomerNotFoundException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
