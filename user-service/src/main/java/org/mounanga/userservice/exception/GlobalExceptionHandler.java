package org.mounanga.userservice.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull UserNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body( new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                List.of()
        ));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull RoleNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body( new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                List.of()
        ));
    }

    @ExceptionHandler(VerificationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull VerificationNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body( new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                List.of()
        ));
    }

    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull UserNotEnabledException exception) {
        return ResponseEntity.status(FORBIDDEN).body( new ExceptionResponse(
                FORBIDDEN.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                List.of()
        ));
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull UserNotAuthenticatedException exception) {
        return ResponseEntity.status(UNAUTHORIZED).body( new ExceptionResponse(
                UNAUTHORIZED.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                List.of()
        ));
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull FieldValidationException exception) {
        return ResponseEntity.status(CONFLICT).body( new ExceptionResponse(
                CONFLICT.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                exception.getFieldErrors().stream().map(FieldError::message).toList()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull MethodArgumentNotValidException exception) {
        Set<String> validationErrors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> validationErrors.add(error.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(
                BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                validationErrors.stream().toList()
        ));
    }


}
