package com.mikky.corebanking.authenticationservice.shared.util;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.authenticationservice.infrastructure.security.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtils implements IJwtUtility {

    private final JwtProperties jwtProperties;
    private final RSAPrivateKey rsaPrivateKey;
    private final RSAPublicKey rsaPublicKey;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String generateJwtToken(UserDetails userDetails) {
        try {
            String roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> authority.startsWith("ROLE_"))
                    .collect(Collectors.joining(","));

            String permissions = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> !authority.startsWith("ROLE_"))
                    .collect(Collectors.joining(","));

            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .claim("roles", roles)
                    .claim("permissions", permissions)
                    .setIssuedAt(new Date())
                    .setExpiration(
                            new Date(System.currentTimeMillis() + Long.parseLong(jwtProperties.getExpirationMs())))
                    .signWith(SignatureAlgorithm.RS256, this.rsaPrivateKey)
                    .compact();
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.rsaPublicKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(this.rsaPublicKey)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception exception) {
            this.logger.error(exception.getMessage());
        }

        return false;
    }

    @Override
    public String getClaimFromToken(String claim, String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.rsaPublicKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get(claim, String.class);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String prefix = "Bearer ";
        if ((headerAuth != null) && (headerAuth.startsWith(prefix))) {
            return headerAuth.substring(prefix.length());
        }
        return null;
    }
}
