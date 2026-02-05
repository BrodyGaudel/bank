package com.mounanga.gatewayservice.exception;

import java.time.LocalDateTime;

public record ExceptionDTO(
        Integer code,
        String message,
        String detail,
        LocalDateTime timestamp)  {
}

