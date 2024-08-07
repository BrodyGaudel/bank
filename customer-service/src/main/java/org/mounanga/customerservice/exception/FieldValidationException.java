package org.mounanga.customerservice.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class FieldValidationException extends RuntimeException {

    private final Set<FieldError> errors;

    public FieldValidationException(String message, Set<FieldError> errors) {
        super(message);
        this.errors = errors;
    }
}
