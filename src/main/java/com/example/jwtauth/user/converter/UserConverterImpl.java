package com.example.jwtauth.user.converter;

import com.example.jwtauth.security.user.model.UserRole;
import com.example.jwtauth.security.user.payload.response.CurrentUserResponse;
import com.example.jwtauth.user.model.User;
import com.example.jwtauth.user.payload.response.UserDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverterImpl implements UserConverter {

    @Override
    public UserDataResponse covertUserToResponse(User user) {
        return new UserDataResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    @Override
    public CurrentUserResponse convertCurrentUserToResponse(User user) {
        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),

                user.getRoles().stream().map(role ->
                        new UserRole(
                                role.getId(),
                                role.getName()
                        )
                ).collect(Collectors.toSet())
        );
    }

}
