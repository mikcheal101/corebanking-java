package com.mikky.corebanking.authenticationservice.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {
    private String[] endpoints;
}
