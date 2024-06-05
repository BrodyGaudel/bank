package org.mounanga.accountservice.command.exception;

public class AmountNotSufficientException extends RuntimeException{

    public AmountNotSufficientException(String message) {
        super(message);
    }
}
