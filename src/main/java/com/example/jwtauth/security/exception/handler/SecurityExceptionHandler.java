package com.example.jwtauth.security.exception.handler;

import com.example.jwtauth.security.exception.ForbiddenException;
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

}
