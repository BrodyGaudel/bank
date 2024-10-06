package org.mounanga.authenticationservice.util.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String LOWERCASE_PATTERN = ".*[a-z].*[a-z].*";
    private static final String UPPERCASE_PATTERN = ".*[A-Z].*[A-Z].*";
    private static final String DIGIT_PATTERN = ".*\\d.*\\d.*";
    private static final String SPECIAL_CHARACTER_PATTERN = ".*[@$!%*?&].*";
    private static final int MINIMUM_LENGTH = 8;

    @Override
    public void initialize(Password constraintAnnotation) {
        //You can reset here if necessary
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.length() < MINIMUM_LENGTH) {
            return false;
        }
        if (!password.matches(LOWERCASE_PATTERN)) {
            return false;
        }
        if (!password.matches(UPPERCASE_PATTERN)) {
            return false;
        }
        if (!password.matches(DIGIT_PATTERN)) {
            return false;
        }
        return password.matches(SPECIAL_CHARACTER_PATTERN);
    }
}

