package com.brodygaudel.customerservice.exception;

/**
 * The {@code CustomerNotFoundException} class is an exception that is thrown to indicate
 * that a customer entity could not be found. This exception is typically used when attempting
 * to retrieve or operate on a customer entity that does not exist in the system.
 * <p>
 * Instances of this exception are created with a specific detail message that describes the
 * reason for the exception.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * try {
 *     // Code that may throw CustomerNotFoundException
 * } catch (CustomerNotFoundException e) {
 *     // Handle the exception, possibly providing user-friendly error messages
 *     // or taking appropriate actions to handle the absence of the customer.
 * }
 * }
 * </pre>
 * </p>
 * @author Brody Gaudel MOUNANGA BOUKA
 * @see Exception
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
