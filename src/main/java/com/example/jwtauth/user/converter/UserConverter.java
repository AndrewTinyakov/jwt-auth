package com.example.jwtauth.user.converter;

import com.example.jwtauth.security.user.payload.response.CurrentUserResponse;
import com.example.jwtauth.user.model.User;
import com.example.jwtauth.user.payload.response.UserDataResponse;

public interface UserConverter {
    UserDataResponse covertUserToResponse(User user);

    CurrentUserResponse convertCurrentUserToResponse(User user);

}
