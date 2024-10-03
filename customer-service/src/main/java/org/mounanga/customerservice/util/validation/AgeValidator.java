package org.mounanga.customerservice.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<AgeMinimum, LocalDate> {

    private int minimumAge;

    @Override
    public void initialize(AgeMinimum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.minimumAge = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        Period period = Period.between(localDate, today);
        int age = period.getYears();

        return age >= minimumAge;
    }
}