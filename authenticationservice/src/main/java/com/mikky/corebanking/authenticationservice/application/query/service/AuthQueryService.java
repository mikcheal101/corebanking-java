package com.mikky.corebanking.authenticationservice.application.query.service;

import org.springframework.stereotype.Service;
import com.mikky.corebanking.authenticationservice.domain.exceptions.InvalidTokenException;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.query.RoleQueryRepository;
import com.mikky.corebanking.authenticationservice.shared.util.JwtUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthQueryService {

    private final JwtUtils jwtUtils;
    private final RoleQueryRepository roleQueryRepository;

    public void validateToken(String authHeader) throws InvalidTokenException {
        String prefix = "Bearer";
        if ((authHeader == null) || !(authHeader.startsWith(prefix))) {
            throw new InvalidTokenException();
        }

        String token = authHeader.substring(prefix.length());
        if (!jwtUtils.validateToken(token)) {
            throw new InvalidTokenException();
        }
    }

    public long countRoles() {
        return this.roleQueryRepository.count();
    }

}
