package com.lamnd.zerotohero.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.lamnd.zerotohero.validator.DobValidator;

@Target({ElementType.FIELD}) // This annotation can only be applied to fields
@Retention(RetentionPolicy.RUNTIME) // This annotation will be available at runtime through reflection
@Constraint(validatedBy = DobValidator.class)
public @interface DobConstraint {
    String message() default "Invalid date of birth"; // default error message when validation fails

    int minAge(); // minimum age requirement

    Class<?>[] groups() default {}; // for grouping validations

    Class<? extends Payload>[] payload() default {}; // for carrying metadata
}
