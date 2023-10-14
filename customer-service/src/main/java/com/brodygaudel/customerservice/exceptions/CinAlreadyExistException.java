package com.brodygaudel.customerservice.exceptions;

/**
 * The {@code CinAlreadyExistException} class is an exception that is thrown to indicate
 * that an attempt to create or update a record failed due to the existence of a passport
 * or national identity card number (CIN) that is already associated with another entity in the system.
 * <p>
 * Instances of this exception are created with a specific detail message that describes the
 * reason for the exception, typically indicating the conflicting CIN.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * try {
 *     // Code that may throw CinAlreadyExistException
 * } catch (CinAlreadyExistException e) {
 *     // Handle the exception, possibly providing user-friendly error messages
 *     // or taking appropriate actions to resolve the CIN conflict.
 * }
 * }
 * </pre>
 * </p>
 *
 * @see Exception
 *
 * @author Brody Gaudel
 */
public class CinAlreadyExistException extends Exception {
    /**
     * Constructs a new {@code CinAlreadyExistException} with the specified detail message.
     * The cause is not initialized and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message The detail message. The detail message is saved for later retrieval
     *                by the {@link #getMessage()} method.
     */
    public CinAlreadyExistException(String message) {
        super(message);
    }
}

