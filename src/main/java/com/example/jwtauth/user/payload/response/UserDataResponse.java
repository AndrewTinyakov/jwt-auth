package com.example.jwtauth.user.payload.response;

import lombok.Builder;

@Builder
public record UserDataResponse(
        Long id,
        String username,
        String email
) {

}
