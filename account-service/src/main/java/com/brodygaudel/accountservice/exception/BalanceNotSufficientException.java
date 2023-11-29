package com.brodygaudel.accountservice.exception;

/**
 * Exception thrown to indicate that a financial operation cannot be completed due to insufficient balance.
 *
 * <p>This exception is typically thrown when an attempt is made to perform a financial operation,
 * such as a withdrawal, and the account does not have a sufficient balance.</p>
 *
 * <p>Author: Brody Gaudel</p>
 */
public class BalanceNotSufficientException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
