package com.mikky.corebanking.audit.infrastructure.security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProperties jwtProperties;

    @Bean
    public RSAPublicKey jwtPublicKey() throws Exception {
        String fileContent = Files.readString(Path.of(this.jwtProperties.getPublicSecretKeyPath()));

        // Extract base64
        String publicKey = fileContent
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");

        byte[] bytes = Base64.getDecoder().decode(publicKey);
        var encoding = new X509EncodedKeySpec(bytes);

        return (RSAPublicKey) KeyFactory.getInstance(this.jwtProperties.getSecretAlgorithm()).generatePublic(encoding);
    }
}
