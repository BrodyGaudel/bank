package org.mounanga.userservice.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class FieldValidationException extends RuntimeException {

    final List<FieldError> fieldErrors;

    public FieldValidationException(List<FieldError> fieldErrors,String message) {
        super(message);
        this.fieldErrors = fieldErrors;
    }
}
