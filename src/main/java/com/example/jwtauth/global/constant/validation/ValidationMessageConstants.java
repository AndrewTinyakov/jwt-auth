package com.example.jwtauth.global.constant.validation;


public class ValidationMessageConstants {

    private static final String SIZE_MESSAGE = " must be at least {min} characters and no more than {max} characters";
    private static final String REQUIRED_MESSAGE = " is required";
    private static final String NOT_PASSED_REGEXP = " haven't passed regexp";

    private static final String EMAIL_FIELD_NAME = "email";
    public static final String EMAIL_MESSAGE = "Must be an " + EMAIL_FIELD_NAME;
    public static final String EMAIL_SIZE_MESSAGE = EMAIL_FIELD_NAME + SIZE_MESSAGE;
    public static final String EMAIL_REQUIRED_MESSAGE = EMAIL_FIELD_NAME + REQUIRED_MESSAGE;

    private static final String PASSWORD_FIELD_NAME = "Password";
    public static final String PASSWORD_SIZE_MESSAGE = PASSWORD_FIELD_NAME + SIZE_MESSAGE;
    public static final String PASSWORD_REQUIRED_MESSAGE = PASSWORD_FIELD_NAME + REQUIRED_MESSAGE;

    private static final String USERNAME_FIELD_NAME = "Username";
    public static final String USERNAME_NOT_PASSED_REGEXP_MESSAGE = USERNAME_FIELD_NAME + NOT_PASSED_REGEXP;
    public static final String USERNAME_SIZE_MESSAGE = USERNAME_FIELD_NAME + SIZE_MESSAGE;
    public static final String USERNAME_REQUIRED_MESSAGE = USERNAME_FIELD_NAME + REQUIRED_MESSAGE;

}
