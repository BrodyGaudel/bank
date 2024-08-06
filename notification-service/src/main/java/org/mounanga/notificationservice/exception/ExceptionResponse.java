package org.mounanga.notificationservice.exception;

import java.util.Set;

public record ExceptionResponse(Integer code,
                                String message,
                                String description,
                                Set<String> validationErrors) {
}
