package com.mikky.corebanking.authenticationservice.shared.util;

import org.springframework.security.core.userdetails.UserDetails;
import jakarta.servlet.http.HttpServletRequest;

public interface IJwtUtility {
    String generateJwtToken(UserDetails userDetails);
    boolean validateToken(String token);
    String parseJwt(HttpServletRequest httpServletRequest);
    String getUsernameFromToken(String token);
    String getClaimFromToken(String claim, String token);
}
