package com.mikky.corebanking.authenticationservice.infrastructure.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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

import com.mikky.corebanking.authenticationservice.shared.util.JwtUtils;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final CorsProperties corsProperties;
    private final SecurityProperties securityProperties;

    @Bean
    public JwtAuthTokenFilter authenticationJwtFilter() {
        return new JwtAuthTokenFilter(this.jwtUtils, this.userDetailsService);
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
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
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
                .authorizeHttpRequests(authorization -> authorization.requestMatchers(securityProperties.getEndpoints()).permitAll()
                        .anyRequest().authenticated());
        httpSecurity.addFilterBefore(this.authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
