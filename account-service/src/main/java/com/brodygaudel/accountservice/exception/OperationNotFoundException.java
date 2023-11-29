package com.brodygaudel.accountservice.exception;

/**
 * Exception thrown to indicate that an operation on a bank account could not be found.
 *
 * <p>This exception is typically thrown when an attempt is made to retrieve details of an
 * operation, and the specified operation cannot be found.</p>
 *
 * <p>An {@code Operation} represents a credit or debit action on a bank account.</p>
 *
 * <p>Author: Brody Gaudel</p>
 */
public class OperationNotFoundException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public OperationNotFoundException(String message) {
        super(message);
    }
}
