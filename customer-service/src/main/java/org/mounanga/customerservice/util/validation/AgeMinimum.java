package org.mounanga.customerservice.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
public @interface AgeMinimum {
    String message() default "Customer does not meet the minimum age requirement";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 16;
}
