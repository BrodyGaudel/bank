package com.mounanga.accountcommandservice.exception;

public class AmountNotSufficientException extends RuntimeException {
    public AmountNotSufficientException(String message) {
        super(message);
    }
}
