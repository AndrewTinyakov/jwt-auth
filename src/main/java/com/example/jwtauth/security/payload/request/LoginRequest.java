package com.example.jwtauth.security.payload.request;

import com.example.jwtauth.user.validation.annotation.PasswordValidation;
import com.example.jwtauth.user.validation.annotation.UsernameValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @UsernameValidation
    private String username;

    @PasswordValidation
    private String password;

}
