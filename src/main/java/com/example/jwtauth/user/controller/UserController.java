package com.example.jwtauth.user.controller;

import com.example.jwtauth.global.exception.NotFoundException;
import com.example.jwtauth.security.user.payload.response.CurrentUserResponse;
import com.example.jwtauth.user.converter.UserConverter;
import com.example.jwtauth.user.model.User;
import com.example.jwtauth.user.payload.response.UserDataResponse;
import com.example.jwtauth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @GetMapping("/{username}")
    public UserDataResponse findUserByUsername(@PathVariable String username) {
        log.debug("Get user request: username={}", username);

        User user = userService.findUserByUsername(username)
                .orElseThrow(NotFoundException::new);
        return userConverter.covertUserToResponse(user);
    }

    @GetMapping("/current-user")
    public CurrentUserResponse getCurrentUser() {
        log.debug("Get current user request");

        User currentUser = userService.getCurrentUser();
        return userConverter.convertCurrentUserToResponse(currentUser);
    }

}