package com.codegrade.restapi.utils.validator;

import com.codegrade.restapi.entity.AssignmentState;

import javax.validation.*;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE_USE})
@Constraint(validatedBy = VAssignmentStateValidator.class)
@Retention(RUNTIME)
public @interface VAssignmentState {
    String message() default "invalid assignment state";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class VAssignmentStateValidator implements ConstraintValidator<VAssignmentState, String> {

    private static final java.util.regex.Pattern uuid_pattern = java.util.regex.Pattern
            .compile("^"+ String.join("|", List.of(
                    AssignmentState.S_DRAFT,
                    AssignmentState.S_PUBLISHED,
                    AssignmentState.S_OPEN,
                    AssignmentState.S_CLOSED,
                    AssignmentState.S_AUTO_OPEN,
                    AssignmentState.S_FINALIZED
            )) +"$");

    @Override
    public void initialize(VAssignmentState constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.isEmpty() || uuid_pattern.matcher(s).matches();
    }
}
