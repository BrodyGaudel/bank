package org.mounanga.accountservice.commands.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class UniquenessValidationException extends RuntimeException{

    private final Set<FieldError> errors;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UniquenessValidationException(String message, Set<FieldError> errors) {
        super(message);
        this.errors = errors;
    }
}
