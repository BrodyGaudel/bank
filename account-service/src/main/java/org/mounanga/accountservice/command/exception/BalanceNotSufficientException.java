package org.mounanga.accountservice.command.exception;

public class BalanceNotSufficientException extends RuntimeException{
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
