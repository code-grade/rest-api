package com.codegrade.restapi.utils.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Optional;
import java.util.regex.Pattern;

@Documented
@Constraint(validatedBy = OptionalUUIDValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalUUID {
    String message() default "invalid uuid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


class OptionalUUIDValidator implements ConstraintValidator<OptionalUUID, Optional<String>> {

    private static final Pattern uuid_pattern = Pattern
            .compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    @Override
    public void initialize(OptionalUUID constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Optional<String> s, ConstraintValidatorContext constraintValidatorContext) {
        return s.isEmpty() || uuid_pattern.matcher(s.get()).matches();
    }
}
