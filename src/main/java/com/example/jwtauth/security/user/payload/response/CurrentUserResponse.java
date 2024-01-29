package com.example.jwtauth.security.user.payload.response;

import com.example.jwtauth.security.user.model.UserRole;

import java.util.Set;

public record CurrentUserResponse(
        Long id,
        String username,
        Set<UserRole> roles
) {

}
