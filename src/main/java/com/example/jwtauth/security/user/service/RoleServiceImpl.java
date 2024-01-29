package com.example.jwtauth.security.user.service;

import com.example.jwtauth.security.user.model.Role;
import com.example.jwtauth.security.user.model.UserRole;
import com.example.jwtauth.security.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void saveRole(UserRole role) {
        roleRepository.save(role);
    }

    @Override
    public Optional<UserRole> findRoleByName(Role roleName) {
        return roleRepository.findByName(roleName);
    }

}
