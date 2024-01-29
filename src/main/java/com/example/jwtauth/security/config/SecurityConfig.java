package com.example.jwtauth.security.config;

import com.example.jwtauth.security.exception.handler.AuthEntryPointJwt;
import com.example.jwtauth.security.token.access.service.AuthTokenFilter;
import com.example.jwtauth.security.token.service.TokenUtils;
import com.example.jwtauth.security.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Order(2)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;
    private final AntPathMatcher antPathMatcher;
    private final SecurityEndpointConfig securityEndpointConfig;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(tokenUtils, userDetailsService, antPathMatcher, securityEndpointConfig);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        (request) -> request
                                .requestMatchers(Arrays.toString(securityEndpointConfig.getUnauthorized().toArray()))
                                .permitAll()
                                .requestMatchers(Arrays.toString(securityEndpointConfig.getAuthorized().toArray()))
                                .authenticated()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        Arrays.toString(securityEndpointConfig.getOptionallyAuthorized().toArray()))
                                .permitAll()
                                .anyRequest()
                                .permitAll()
                )
                .sessionManagement((securityContext) -> securityContext
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf((csrfConfigurer) -> csrfConfigurer
//                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                                .ignoringRequestMatchers(Arrays.toString(securityEndpointConfig.getUnauthorized().toArray()))
//                )
//todo
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        (configurer ->
                                configurer.authenticationEntryPoint(unauthorizedHandler)
                        )
                )
                .build();
    }

}
