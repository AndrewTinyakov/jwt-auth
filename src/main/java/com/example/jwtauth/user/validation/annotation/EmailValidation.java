package com.example.jwtauth.user.validation.annotation;

import com.example.jwtauth.global.constant.validation.ValidationMessageConstants;
import com.example.jwtauth.global.constant.validation.ValidationSizeConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull(message = ValidationMessageConstants.EMAIL_REQUIRED_MESSAGE)
@NotBlank(message = ValidationMessageConstants.EMAIL_REQUIRED_MESSAGE)
@Email(message = ValidationMessageConstants.EMAIL_MESSAGE)
@Size(
        min = ValidationSizeConstants.EMAIL_MIN_SIZE,
        max = ValidationSizeConstants.EMAIL_MAX_SIZE,
        message = ValidationMessageConstants.EMAIL_SIZE_MESSAGE
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValidation {
    String message() default "Invalid email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
