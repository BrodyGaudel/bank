package com.brodygaudel.accountservice.exception;

/**
 * Exception thrown to indicate that an account was not found.
 *
 * <p>This exception is typically thrown when an attempt is made to access or manipulate an
 * account that does not exist.</p>
 *
 * <p>Author: Brody Gaudel</p>
 */
public class AccountNotFoundException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
