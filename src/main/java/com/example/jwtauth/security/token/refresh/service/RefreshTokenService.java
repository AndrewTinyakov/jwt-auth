package com.example.jwtauth.security.token.refresh.service;

import com.example.jwtauth.security.token.refresh.model.RefreshToken;
import org.springframework.http.ResponseCookie;

import java.util.Optional;


public interface RefreshTokenService {

    void updateTokenValueAndEncode(String value, Long id);

    Optional<RefreshToken> findById(String id);

    boolean existsById(Long id);

    ResponseCookie createRefreshTokenByUserId(long userId);

    void deleteTokenById(long id);
}
