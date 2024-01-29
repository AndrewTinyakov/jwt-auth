package com.example.jwtauth.security.token.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface TokenUtils {
    ResponseCookie generateJwtCookieByUserId(long userId);

    ResponseCookie generateRefreshTokenCookie(long tokenId);

    String getAccessTokenFromCookies(HttpServletRequest request);

    String getRefreshTokenFromCookies(HttpServletRequest request);

    ResponseCookie getCleanAccessTokenCookie();

    ResponseCookie getCleanRefreshTokenCookie();

    String getIdFromAccessToken(String token);

    String getIdFromRefreshToken(String token);

    boolean validateJwtAccessToken(String authToken);

    boolean validateJwtRefreshToken(String refreshToken);
}
