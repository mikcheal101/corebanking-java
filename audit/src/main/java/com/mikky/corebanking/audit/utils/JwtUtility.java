package com.mikky.corebanking.audit.utils;

import java.security.interfaces.RSAPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtility implements IJwtUtility {

    private final RSAPublicKey rsaPublicKey;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(this.rsaPublicKey)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public String parseJwt(HttpServletRequest httpServletRequest) {
        String headerPrefix = "Bearer ";
        var header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith(headerPrefix)) {
            return header.substring(headerPrefix.length());
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
}
