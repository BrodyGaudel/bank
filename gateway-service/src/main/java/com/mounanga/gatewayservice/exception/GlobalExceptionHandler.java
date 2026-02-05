package com.mounanga.gatewayservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = TimeoutException.class)
    public ResponseEntity<@NotNull ExceptionDTO> handle(@NotNull TimeoutException exception) {
        ExceptionDTO dto = new ExceptionDTO(
                GATEWAY_TIMEOUT.value(),
                "Le service a mis trop de temps à répondre.",
                exception.getMessage(),
                LocalDateTime.now()
        );
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(GATEWAY_TIMEOUT).body(dto);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<@NotNull ExceptionDTO> handle(@NotNull NotFoundException exception) {
        ExceptionDTO dto = new ExceptionDTO(
                NOT_FOUND.value(),
                "Le service demandé n'existe pas. Vérifiez le nom du service dans Consul",
                exception.getMessage(),
                LocalDateTime.now()
        );
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(NOT_FOUND).body(dto);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<@NotNull ExceptionDTO> handle(@NotNull Exception exception) {
        log.error(exception.getMessage(), exception);
        if(exception.getMessage() != null && exception.getMessage().contains("Unable to find instance")) {
            ExceptionDTO dto = new ExceptionDTO(
                    SERVICE_UNAVAILABLE.value(),
                    "Le service est temporairement indisponible. Toutes les instances sont arrêtées ou inaccessibles",
                    exception.getMessage(),
                    LocalDateTime.now()
            );
            return ResponseEntity.status(SERVICE_UNAVAILABLE).body(dto);
        } else if (exception.getMessage() != null && exception.getMessage().contains("404 NOT_FOUND")) {
            ExceptionDTO dto = new ExceptionDTO(
                    NOT_FOUND.value(),
                    "Le service et/ou la ressource demandé n'existe pas. Il ce peut aussi qu'elle soit temporairement indisponible.",
                    exception.getMessage(),
                    LocalDateTime.now()
            );
            return ResponseEntity.status(NOT_FOUND).body(dto);
        } else{
            ExceptionDTO dto = new ExceptionDTO(
                    INTERNAL_SERVER_ERROR.value(),
                    exception.getMessage(),
                    exception.getLocalizedMessage(),
                    LocalDateTime.now()
            );
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(dto);
        }

    }

}
