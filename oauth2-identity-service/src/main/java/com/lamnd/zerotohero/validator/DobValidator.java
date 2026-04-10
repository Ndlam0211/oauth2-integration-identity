package com.lamnd.zerotohero.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.lamnd.zerotohero.annotation.DobConstraint;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int minAge;

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        this.minAge = constraintAnnotation.minAge(); // get the minimum age from the annotation
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (Objects.isNull(dob)) {
            return true; // consider null as valid, use @NotNull for null checks
        }
        long userYearsOld = ChronoUnit.YEARS.between(
                dob,
                LocalDate
                        .now()); // calculate the number of years between the date that user submit and the current date
        return userYearsOld >= minAge; // return true if the user is old enough, otherwise return false
    }
}
