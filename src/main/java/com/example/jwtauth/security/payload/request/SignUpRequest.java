package com.example.jwtauth.security.payload.request;

import com.example.jwtauth.user.validation.annotation.EmailValidation;
import com.example.jwtauth.user.validation.annotation.PasswordValidation;
import com.example.jwtauth.user.validation.annotation.UsernameValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @UsernameValidation
    private String username;

    @EmailValidation
    private String email;

    @PasswordValidation
    private String password;

}
