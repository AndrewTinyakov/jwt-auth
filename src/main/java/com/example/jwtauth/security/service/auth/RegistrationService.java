package com.example.jwtauth.security.service.auth;


import com.example.jwtauth.security.payload.request.SignUpRequest;

public interface RegistrationService {
    void registration(SignUpRequest signUpRequest);

}
