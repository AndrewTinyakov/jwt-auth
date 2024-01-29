package com.example.jwtauth.security.token.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class TokenUtilsImpl implements TokenUtils {

    @Value("${jwt.access-secret}")
    private String accessJwtSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshJwtSecret;

    @Value("${jwt.expiration-ms}")
    private int accessExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Value("${jwt.access-token-name}")
    private String accessTokenName;

    @Value("${jwt.refresh-token-name}")
    private String refreshTokenName;

    @Override
    public ResponseCookie generateJwtCookieByUserId(long userId) {
        String jwt = generateAccessTokenFromId(userId);
        return generateCookie(accessTokenName, jwt, false);
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(long tokenId) {
        String refreshToken = generateRefreshTokenFromId(tokenId);
        return generateCookie(refreshTokenName, refreshToken, true);
    }

    @Override
    public String getAccessTokenFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, accessTokenName);
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, refreshTokenName);
    }

    @Override
    public ResponseCookie getCleanAccessTokenCookie() {
        return ResponseCookie.from(accessTokenName, "")
                .path("/")
                .build();
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .build();
    }

    @Override
    public String getIdFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getAccessSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public String getIdFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String generateAccessTokenFromId(Long id) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        new Date().getTime() + accessExpirationMs)
                )
                .signWith(getAccessSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private String generateRefreshTokenFromId(Long id) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        new Date().getTime() + refreshExpirationMs)
                )
                .signWith(getRefreshSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean validateJwtAccessToken(String authToken) {
        if (authToken == null) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getAccessSignInKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    @Override
    public boolean validateJwtRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getRefreshSignInKey())
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private ResponseCookie generateCookie(String name, String value, boolean httpOnly) {
        return ResponseCookie.from(name, value)
                .path("/")
                .maxAge(24 * 60 * 60)
                .httpOnly(httpOnly)
                .sameSite("Strict")
                .build();
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    private Key getAccessSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessJwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getRefreshSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshJwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
