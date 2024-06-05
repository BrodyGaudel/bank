package org.mounanga.accountservice.common.exception;


import org.axonframework.queryhandling.QueryExecutionException;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.command.exception.AccountNotActivatedException;
import org.mounanga.accountservice.command.exception.AmountNotSufficientException;
import org.mounanga.accountservice.command.exception.BalanceNotSufficientException;
import org.mounanga.accountservice.command.exception.CustomerNotFoundException;
import org.mounanga.accountservice.query.exception.AccountNotFoundException;
import org.mounanga.accountservice.query.exception.OperationNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(@NotNull MethodArgumentNotValidException exception) {
        Set<String> validationErrors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> validationErrors.add(error.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(
                BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                validationErrors,
                new HashMap<>()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneralException(@NotNull Exception exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<ExceptionResponse> handleAccountNotActivatedException(@NotNull AccountNotActivatedException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(
                BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(AmountNotSufficientException.class)
    public ResponseEntity<ExceptionResponse> handleAmountNotSufficientException(@NotNull AmountNotSufficientException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(
                BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(BalanceNotSufficientException.class)
    public ResponseEntity<ExceptionResponse> handleBalanceNotSufficientException(@NotNull BalanceNotSufficientException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(
                BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCustomerNotFoundException(@NotNull CustomerNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAccountNotFoundException(@NotNull AccountNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotFoundException(@NotNull OperationNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(QueryExecutionException.class)
    public ResponseEntity<ExceptionResponse> handleOperationNotFoundException(@NotNull QueryExecutionException exception) {
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }
}

