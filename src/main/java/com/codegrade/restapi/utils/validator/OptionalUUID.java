package com.codegrade.restapi.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OptionalUUIDValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalUUID {
    String message() default "invalid uuid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
