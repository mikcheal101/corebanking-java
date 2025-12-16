package com.mikky.corebanking.authenticationservice.shared.util;

import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.authenticationservice.security.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public String generateJwtToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .claim("roles", roles)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtProperties.getExpirationMs())))
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
            .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(jwtProperties.getSecret())
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public String getRolesFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(jwtProperties.getSecret())
            .parseClaimsJws(token)
            .getBody()
            .get("roles", String.class);
    }

}
