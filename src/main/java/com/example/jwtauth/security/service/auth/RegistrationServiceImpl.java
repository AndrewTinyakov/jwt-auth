package com.example.jwtauth.security.service.auth;

import com.example.jwtauth.global.config.utils.AppInit;
import com.example.jwtauth.security.exception.registration.TakenEmailException;
import com.example.jwtauth.security.exception.registration.TakenUsernameAndEmailException;
import com.example.jwtauth.security.exception.registration.TakenUsernameException;
import com.example.jwtauth.security.payload.request.SignUpRequest;
import com.example.jwtauth.security.user.model.UserRole;
import com.example.jwtauth.security.user.service.RoleService;
import com.example.jwtauth.user.model.User;
import com.example.jwtauth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.example.jwtauth.security.user.model.Role.ROLE_USER;


@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;
    private final RoleService roleService;
    private final AppInit appInit;

    @Override
    @Transactional
    public void registration(SignUpRequest signUpRequest) {
        validateRegistration(signUpRequest);
        User user = createUser(signUpRequest);
        log.debug("User registered: id={}, username={}", user.getId(), user.getUsername());
    }

    private void validateRegistration(SignUpRequest request) {
        boolean existsByEmail = userService.existsByEmail(request.getEmail());
        boolean existsByUsername = userService.existsByUsername(request.getUsername());

        if (existsByEmail && existsByUsername) {
            log.warn("User was not registered: taken username and email");
            throw new TakenUsernameAndEmailException();
        }
        if (existsByUsername) {
            log.warn("User was not registered: taken username");
            throw new TakenUsernameException();
        }
        if (existsByEmail) {
            log.warn("User was not registered: taken email");
            throw new TakenEmailException();
        }
    }

    private User createUser(SignUpRequest signUpRequest) {
        Set<UserRole> roles = new HashSet<>();
        UserRole userRole = roleService.findRoleByName(ROLE_USER)
                .orElse(null);
        if (userRole == null) {
            log.warn("Roles are null");
            appInit.insertRoles();
            userRole = roleService.findRoleByName(ROLE_USER)
                    .orElseThrow();
        }
        roles.add(userRole);
        User user = new User(signUpRequest, roles);

        return userService.register(user);
    }

}
