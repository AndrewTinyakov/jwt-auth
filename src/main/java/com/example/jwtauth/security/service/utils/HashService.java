package com.example.jwtauth.security.service.utils;

public interface HashService {
    String hashStringWithSHA256(String input);

    boolean verifySHA256Hash(String value, String hashedValue);
}
