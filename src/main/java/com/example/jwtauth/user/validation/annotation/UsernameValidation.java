package com.example.jwtauth.user.validation.annotation;

import com.example.jwtauth.global.constant.validation.ValidationMessageConstants;
import com.example.jwtauth.global.constant.validation.ValidationRegexpConstants;
import com.example.jwtauth.global.constant.validation.ValidationSizeConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull(message = ValidationMessageConstants.USERNAME_REQUIRED_MESSAGE)
@NotBlank(message = ValidationMessageConstants.USERNAME_REQUIRED_MESSAGE)
@Size(
        min = ValidationSizeConstants.USERNAME_MIN_SIZE,
        max = ValidationSizeConstants.USERNAME_MAX_SIZE,
        message = ValidationMessageConstants.USERNAME_SIZE_MESSAGE
)
@Pattern(
        regexp = ValidationRegexpConstants.ENGLISH_LETTERS_AND_ARABIC_NUMBERS_REGEXP,
        message = ValidationMessageConstants.USERNAME_NOT_PASSED_REGEXP_MESSAGE
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameValidation {
    String message() default "Invalid username";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
