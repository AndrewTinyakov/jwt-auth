package com.example.jwtauth.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security.endpoints")
@Getter
@Setter
@Order(1)
@AllArgsConstructor
@NoArgsConstructor
public class SecurityEndpointConfig {

    private List<String> unauthorized;

    private List<String> authorized;

    private List<String> optionallyAuthorized;

}
