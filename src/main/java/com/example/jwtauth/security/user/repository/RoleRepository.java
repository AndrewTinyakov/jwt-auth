package com.example.jwtauth.security.user.repository;

import com.example.jwtauth.security.user.model.Role;
import com.example.jwtauth.security.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(Role name);
}
