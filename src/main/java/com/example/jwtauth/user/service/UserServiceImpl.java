package com.example.jwtauth.user.service;

import com.example.jwtauth.security.exception.ForbiddenException;
import com.example.jwtauth.security.user.model.UserDetailsImpl;
import com.example.jwtauth.user.model.User;
import com.example.jwtauth.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.jwtauth.security.constraint.exception.AuthExceptionMessageConstants.BAD_CREDENTIALS;


@Service
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findOptionalUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        user.ifPresent(value -> log.debug("Found user by id: id={}, username={}",
                value.getId(), value.getUsername()));

        return user;
    }

    @Override
    @Transactional
    public User register(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User savedUser = userRepository.save(user);

        log.debug("Registered user: id={}, username={}", savedUser.getId(), savedUser.getUsername());
        return savedUser;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();

        User currentUser = findOptionalUserById(principal.getId())
                .orElseThrow(ForbiddenException::new);

        log.debug("Got current user: id={}, username={}", currentUser.getId(), currentUser.getUsername());
        return currentUser;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        user.ifPresent(value -> log.debug("Found user by username: id={}, username={}", value.getId(), value.getUsername()));
        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        boolean exists = userRepository.existsByUsernameIgnoreCase(username.toLowerCase());

        log.debug("Found if user exists by username: username={}", username);

        return exists;
    }

    @Override
    public boolean existsByEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);

        log.debug("Found if user exists by email: email={}", email);

        return exists;
    }

}
