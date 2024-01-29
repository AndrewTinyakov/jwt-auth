package com.example.jwtauth.global.config.utils;

import com.example.jwtauth.security.user.model.UserRole;
import com.example.jwtauth.security.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.jwtauth.security.user.model.Role.ROLE_ADMIN;
import static com.example.jwtauth.security.user.model.Role.ROLE_USER;

@Component
@RequiredArgsConstructor
public class AppInitImpl implements AppInit {

    private final RoleService roleService;

    @Override
    public void insertRoles() {
        roleService.saveRole(new UserRole(ROLE_ADMIN));
        roleService.saveRole(new UserRole(ROLE_USER));
    }

}
