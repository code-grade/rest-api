package com.codegrade.restapi.utils.validator;

import com.codegrade.restapi.entity.AssignmentType;

import javax.validation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE_USE})
@Constraint(validatedBy = VAssignmentTypeValidator.class)
@Retention(RUNTIME)
public @interface VAssignmentType {
    String message() default "invalid assignment state";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class VAssignmentTypeValidator implements ConstraintValidator<VAssignmentType, String> {

    private static final java.util.regex.Pattern uuid_pattern = java.util.regex.Pattern
            .compile("^" + String.join("|", List.of(
                    AssignmentType.T_PRIVATE,
                    AssignmentType.T_PUBLIC
            )) + "$");

    @Override
    public void initialize(VAssignmentType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.isEmpty() || uuid_pattern.matcher(s).matches();
    }
}
