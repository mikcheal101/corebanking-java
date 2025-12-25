package com.mikky.corebanking.authenticationservice.infrastructure.security;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import com.mikky.corebanking.authenticationservice.shared.util.IJwtUtility;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final CorsProperties corsProperties;
    private final SecurityProperties securityProperties;
    private final JwtProperties jwtProperties;

    @Bean
    public JwtAuthTokenFilter authenticationJwtFilter(IJwtUtility jwtUtils) {
        return new JwtAuthTokenFilter(jwtUtils, this.userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, IJwtUtility jwtUtils) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(corsProperties.getAllowedOrigins());
                    corsConfig.setAllowedMethods(corsProperties.getAllowedMethods());
                    corsConfig.setAllowedHeaders(corsProperties.getAllowedHeaders());
                    corsConfig.setAllowCredentials(corsProperties.isAllowedCredentials());
                    return corsConfig;
                }))
                .authorizeHttpRequests(
                        authorization -> authorization.requestMatchers(securityProperties.getEndpoints()).permitAll()
                                .anyRequest().authenticated());
        httpSecurity.addFilterBefore(this.authenticationJwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public RSAPrivateKey jwtPrivateKey() throws Exception {
        String fileContents = Files.readString(Path.of(this.jwtProperties.getPrivateSecretKeyPath()));

        // replace the unwanted
        String privateKeyPem = fileContents
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");

        byte[] bytes = Base64.getDecoder().decode(privateKeyPem);

        var specification = new PKCS8EncodedKeySpec(bytes);
        return (RSAPrivateKey) KeyFactory.getInstance(this.jwtProperties.getSecretAlgorithm())
                .generatePrivate(specification);
    }

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
