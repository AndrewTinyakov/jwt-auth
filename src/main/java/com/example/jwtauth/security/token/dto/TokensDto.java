package com.example.jwtauth.security.token.dto;

import org.springframework.http.ResponseCookie;

public record TokensDto(
        ResponseCookie getAccessTokenCookie,
        ResponseCookie getRefreshTokenCookie
) {
}
