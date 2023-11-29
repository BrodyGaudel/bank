package com.brodygaudel.accountservice.exception;

/**
 * Exception thrown to indicate that an operation cannot be performed on a non-activated account.
 *
 * <p>This exception is typically thrown when an attempt is made to perform an action on an
 * account that has not been activated.</p>
 *
 * <p>Author: Brody Gaudel</p>
 */
public class AccountNotActivatedException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public AccountNotActivatedException(String message) {
        super(message);
    }
}
