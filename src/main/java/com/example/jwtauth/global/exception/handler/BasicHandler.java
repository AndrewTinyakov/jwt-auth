package com.example.jwtauth.global.exception.handler;

import com.example.jwtauth.global.exception.NotFoundException;
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
public class BasicHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException() {
        log.warn("NotFoundException");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Not found");
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, OK);
    }


}
