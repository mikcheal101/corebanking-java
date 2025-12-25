package com.mikky.corebanking.authenticationservice.infrastructure.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.mikky.corebanking.authenticationservice.shared.util.IJwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final IJwtUtility jwtUtility;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = this.jwtUtility.parseJwt(request);
            if ((jwt != null) && (this.jwtUtility.validateToken(jwt))) {
                String username = this.jwtUtility.getUsernameFromToken(jwt);

                // Load user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Extract roles and permissions.
                var roles = this.jwtUtility.getClaimFromToken("roles", jwt);
                var permissions = this.jwtUtility.getClaimFromToken("permissions", jwt);

                // Convert roles and permissions into authorities
                var authorities = Stream.concat(
                        Arrays.stream(roles.split(",")),
                        Arrays.stream(permissions.split(",")))
                        .filter(auth -> !auth.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

}
