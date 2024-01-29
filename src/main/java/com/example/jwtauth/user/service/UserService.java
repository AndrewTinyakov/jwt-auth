package com.example.jwtauth.user.service;

import com.example.jwtauth.user.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findOptionalUserById(Long id);

    User register(User user);

    User getCurrentUser();

    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


}
