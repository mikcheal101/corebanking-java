package com.mikky.corebanking.audit.infrastructure.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.mikky.corebanking.audit.utils.IJwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final IJwtUtility jwtUtility;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = this.jwtUtility.parseJwt(httpServletRequest);
            if ((token != null) && (this.jwtUtility.validateToken(token))) {
                var username = this.jwtUtility.getUsernameFromToken(token);

                // Fetch roles and permission
                var roles = this.jwtUtility.getClaimFromToken("roles", token);
                var permissions = this.jwtUtility.getClaimFromToken("permissions", token);

                // Convert roles and permissions to authorities
                var authorities = Stream.concat(
                        Arrays.stream(roles.split(",")),
                        Arrays.stream(permissions.split(",")))
                        .filter(auth -> !auth.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
    }
}
