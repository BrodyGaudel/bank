package org.mounanga.customerservice.exception;

import java.util.Set;

public record FieldErrorResponse(Integer code,
                                 String message,
                                 String description,
                                 Set<FieldError> fieldErrors) {
}
