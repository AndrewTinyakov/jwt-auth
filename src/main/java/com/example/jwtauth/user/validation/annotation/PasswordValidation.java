package com.example.jwtauth.user.validation.annotation;

import com.example.jwtauth.global.constant.validation.ValidationMessageConstants;
import com.example.jwtauth.global.constant.validation.ValidationSizeConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull(message = ValidationMessageConstants.PASSWORD_REQUIRED_MESSAGE)
@NotBlank(message = ValidationMessageConstants.PASSWORD_REQUIRED_MESSAGE)
@Size(
        min = ValidationSizeConstants.PASSWORD_MIN_SIZE,
        max = ValidationSizeConstants.PASSWORD_MAX_SIZE,
        message = ValidationMessageConstants.PASSWORD_SIZE_MESSAGE
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidation {

    String message() default "Invalid password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}







