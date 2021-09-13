package com.codegrade.restapi.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * @author zhumaer
 * @ desc check the validity of enumeration value
 * @since 10/17/2017 3:13 PM
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumConstraint.Validator.class)
public @interface EnumConstraint {

    String message() default "{custom.value.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();

    class Validator implements ConstraintValidator<EnumConstraint, Object> {

        private List<String> enumEntries;

        @Override
        public void initialize(EnumConstraint enumAnnotation) {
            enumEntries = new ArrayList<String>();
            Class<? extends Enum<?>> enumClass = enumAnnotation.enumClass();

            @SuppressWarnings("rawtypes")
            Enum[] enumValArr = enumClass.getEnumConstants();

            for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
                enumEntries.add(enumVal.toString().toUpperCase());
            }
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null) {
                return Boolean.TRUE;
            }
            return this.enumEntries.contains(value.toString().toUpperCase());
        }

    }
}