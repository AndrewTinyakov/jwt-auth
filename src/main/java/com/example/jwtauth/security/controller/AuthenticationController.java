package com.example.jwtauth.security.controller;

import com.example.jwtauth.security.payload.request.LoginRequest;
import com.example.jwtauth.security.payload.request.SignUpRequest;
import com.example.jwtauth.security.service.auth.LoginService;
import com.example.jwtauth.security.service.auth.RegistrationService;
import com.example.jwtauth.security.token.dto.TokensDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final LoginService loginService;
    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public void registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        log.debug("Register request: username={}", signUpRequest.getUsername());

        registrationService.registration(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        log.debug("Login request: username={}", loginRequest.getUsername());

        TokensDto loginResponse = loginService.login(loginRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, loginResponse.getAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, loginResponse.getRefreshTokenCookie().toString())
                .body(null);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        log.debug("Refresh token request");

        TokensDto response = loginService.refreshToken(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, response.getRefreshTokenCookie().toString())
                .body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest httpServletRequest) {
        log.debug("Logout user request");

        TokensDto response = loginService.logout(httpServletRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.getAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, response.getRefreshTokenCookie().toString())
                .body(null);
    }

}
