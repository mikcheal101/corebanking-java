package com.mikky.corebanking.authenticationservice.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secret;
    private String expirationMs;

    private String publicSecretKeyPath;
    private String privateSecretKeyPath;
    private String secretAlgorithm;
}
