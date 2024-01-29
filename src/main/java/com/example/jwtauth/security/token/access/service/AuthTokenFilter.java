package com.example.jwtauth.security.token.access.service;

import com.example.jwtauth.security.config.SecurityEndpointConfig;
import com.example.jwtauth.security.token.dto.EndpointType;
import com.example.jwtauth.security.token.service.TokenUtils;
import com.example.jwtauth.security.user.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.jwtauth.security.constraint.exception.AuthExceptionMessageConstants.BAD_CREDENTIALS;


@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final CustomUserDetailsService userDetailsService;
    private final AntPathMatcher antPathMatcher;
    private final ObjectMapper mapper = new ObjectMapper();
    private final SecurityEndpointConfig securityEndpointConfig;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            EndpointType endpointType = getEndpointType(request);
            boolean valid = tokenUtils.validateJwtAccessToken(jwt);

            switch (endpointType) {
                case OPTIONALLY_AUTHORIZED -> {
                    if (jwt != null && !jwt.isBlank()) {
                        if (!valid) {
                            throw new BadCredentialsException(BAD_CREDENTIALS);
                        } else {
                            authorize(jwt, request);
                        }
                    }
                }
                case AUTHORIZED -> {
                    if (!valid) {
                        throw new BadCredentialsException(BAD_CREDENTIALS);
                    }
                    authorize(jwt, request);
                }
            }
        } catch (BadCredentialsException ex) {
            SecurityContextHolder.clearContext();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);

            final Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", ex.getMessage());

            mapper.writeValue(response.getOutputStream(), body);

            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authorize(String jwt, HttpServletRequest request) {
        String id = tokenUtils.getIdFromAccessToken(jwt);
        UserDetails userDetails = userDetailsService.loadUserById(id);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String parseJwt(HttpServletRequest request) {
        return tokenUtils.getAccessTokenFromCookies(request);
    }

    private EndpointType getEndpointType(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        if (securityEndpointConfig.getUnauthorized().stream()
                .anyMatch(pattern -> antPathMatcher.match(pattern, requestURI))) {
            return EndpointType.UNAUTHORIZED;
        }
        if (securityEndpointConfig.getAuthorized().stream()
                .anyMatch(pattern -> antPathMatcher.match(pattern, requestURI))) {
            return EndpointType.AUTHORIZED;
        }
        if (securityEndpointConfig.getOptionallyAuthorized().stream()
                .anyMatch(pattern -> antPathMatcher.match(pattern, requestURI) ||
                        HttpMethod.GET.matches(request.getMethod()))) {
            return EndpointType.OPTIONALLY_AUTHORIZED;
        }
        return EndpointType.AUTHORIZED;
    }
}
