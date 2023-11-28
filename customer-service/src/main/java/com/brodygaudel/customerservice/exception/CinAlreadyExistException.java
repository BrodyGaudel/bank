package com.brodygaudel.customerservice.exception;

/**
 * The {@code EmailAlreadyExistException} class is an exception that is thrown to indicate
 * that an attempt to create or update a record failed due to the existence of an email address
 * that is already associated with another entity in the system.
 * <p>
 * Instances of this exception are created with a specific detail message that describes the
 * reason for the exception, typically indicating the conflicting email address.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * try {
 *     // Code that may throw EmailAlreadyExistException
 * } catch (EmailAlreadyExistException e) {
 *     // Handle the exception, possibly providing user-friendly error messages
 *     // or taking appropriate actions to resolve the email conflict.
 * }
 * }
 * </pre>
 * </p>
 * @author Brody Gaudel MOUNANGA BOUKA
 * @see Exception
 */
public class CinAlreadyExistException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CinAlreadyExistException(String message) {
        super(message);
    }
}
