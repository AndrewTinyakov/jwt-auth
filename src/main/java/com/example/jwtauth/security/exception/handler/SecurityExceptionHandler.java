package com.example.jwtauth.security.exception.handler;

import com.example.jwtauth.security.exception.ForbiddenException;
import com.example.jwtauth.security.exception.registration.TakenEmailException;
import com.example.jwtauth.security.exception.registration.TakenUsernameAndEmailException;
import com.example.jwtauth.security.exception.registration.TakenUsernameException;
import com.example.jwtauth.security.token.refresh.exception.ExpiredRefreshTokenException;
import com.example.jwtauth.security.token.refresh.exception.InvalidRefreshTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@ControllerAdvice
@Slf4j
public class SecurityExceptionHandler {

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException() {
        log.error("ForbiddenException");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Forbidden");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, OK);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<?> handleExpiredRefreshTokenException() {
        log.warn("ExpiredRefreshTokenException");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Refresh token was expired");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, OK);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<?> handleInvalidRefreshTokenException() {
        log.warn("InvalidRefreshTokenException");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Refresh token is invalid");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, OK);
    }

    @ExceptionHandler(TakenUsernameAndEmailException.class)
    public ResponseEntity<?> handleTakenUsernameAndEmailException() {
        log.warn("TakenUsernameAndEmailException");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Username and email are taken");
        body.put("type", "both");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, OK);
    }

    @ExceptionHandler(TakenEmailException.class)
    public ResponseEntity<?> handleTakenEmailException() {
        log.warn("TakenEmailException");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Email is taken");
        body.put("type", "email");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, OK);
    }

    @ExceptionHandler(TakenUsernameException.class)
    public ResponseEntity<?> handleTakenUsernameException() {
        log.warn("TakenUsernameException");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Username is taken");
        body.put("type", "username");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, OK);
    }

}
