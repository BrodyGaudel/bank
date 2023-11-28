package com.brodygaudel.customerservice.exception;

/**
 * The {@code PhoneAlreadyExistException} class is an exception that is thrown to indicate
 * that an attempt to create or update a record failed due to the existence of a phone number
 * that is already associated with another entity in the system.
 * <p>
 * Instances of this exception are created with a specific detail message that describes the
 * reason for the exception, typically indicating the conflicting phone number.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * try {
 *     // Code that may throw PhoneAlreadyExistException
 * } catch (PhoneAlreadyExistException e) {
 *     // Handle the exception, possibly providing user-friendly error messages
 *     // or taking appropriate actions to resolve the phone number conflict.
 * }
 * }
 * </pre>
 * </p>
 *
 * @see Exception
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 */
public class PhoneAlreadyExistException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public PhoneAlreadyExistException(String message) {
        super(message);
    }
}
