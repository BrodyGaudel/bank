package org.mounanga.authenticationservice.exception;

public class VerificationNotFoundException extends RuntimeException {
    public VerificationNotFoundException(String message) {
        super(message);
    }
}
