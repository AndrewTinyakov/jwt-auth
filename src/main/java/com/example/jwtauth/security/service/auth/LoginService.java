package com.example.jwtauth.security.service.auth;

import com.example.jwtauth.security.payload.request.LoginRequest;
import com.example.jwtauth.security.token.dto.TokensDto;
import jakarta.servlet.http.HttpServletRequest;


public interface LoginService {
    TokensDto login(LoginRequest loginRequest) ;

    TokensDto logout(HttpServletRequest httpServletRequest);

    TokensDto refreshToken(HttpServletRequest request);

}
