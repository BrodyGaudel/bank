package org.mounanga.userservice.exception;

import java.util.List;

public record ExceptionResponse(Integer code,
                                String message,
                                String description,
                                List<String> validationErrors)  {
}
