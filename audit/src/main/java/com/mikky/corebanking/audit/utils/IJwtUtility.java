package com.mikky.corebanking.audit.utils;

import jakarta.servlet.http.HttpServletRequest;

public interface IJwtUtility {
    boolean validateToken(String token);
    String parseJwt(HttpServletRequest httpServletRequest);
    String getUsernameFromToken(String token);
    String getClaimFromToken(String claim, String token);
}
