package com.demo.elasticsearch.metadata;


import com.demo.elasticsearch.validator.DocumentDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE, PARAMETER})
@Constraint(validatedBy = DocumentDateValidator.class)
public @interface DocumentDate {

    String message() default "the date is validated";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
