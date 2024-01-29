package com.example.jwtauth.security.user.service;

import com.example.jwtauth.security.user.model.UserDetailsImpl;
import com.example.jwtauth.user.model.User;
import com.example.jwtauth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.jwtauth.security.constraint.exception.AuthExceptionMessageConstants.BAD_CREDENTIALS;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User currentUserView = userService.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException(BAD_CREDENTIALS));

        return UserDetailsImpl.build(currentUserView);
    }

    @Override
    public UserDetails loadUserById(String id) {
        User currentUserView = userService.findOptionalUserById(Long.parseLong(id))
                .orElseThrow(() -> new BadCredentialsException(BAD_CREDENTIALS));

        return UserDetailsImpl.build(currentUserView);
    }

}
