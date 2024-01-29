package com.example.jwtauth.security.user.service;


import com.example.jwtauth.security.user.model.Role;
import com.example.jwtauth.security.user.model.UserRole;

import java.util.Optional;

public interface RoleService {
    void saveRole(UserRole role);

    Optional<UserRole> findRoleByName(Role roleName);

}
