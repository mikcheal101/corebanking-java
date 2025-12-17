package com.mikky.corebanking.authenticationservice.application.command.service;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.mikky.corebanking.authenticationservice.domain.exceptions.InvalidTokenException;
import com.mikky.corebanking.authenticationservice.domain.model.BlacklistedToken;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.command.BlacklistedTokenCommandRepository;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.query.BlacklistedTokenQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlacklistCommandService {

    private final BlacklistedTokenQueryRepository blacklistedTokenQueryRepository;
    private final BlacklistedTokenCommandRepository blacklistedTokenCommandRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional
    public void blackListToken(String authHeader) throws InvalidTokenException {

        String prefix = "Bearer ";
        if ((authHeader == null) || (!authHeader.startsWith(prefix))) {
            throw new InvalidTokenException();
        }

        String token = authHeader.substring(prefix.length());
        Instant expiryTime = Instant.now();
        try {
            var blackListedToken = new BlacklistedToken();
            blackListedToken.setToken(token);
            blackListedToken.setExpiry(expiryTime);
            blacklistedTokenCommandRepository.save(blackListedToken);            
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InvalidTokenException();
        }
    }

    public boolean isBlackListed(String token) {
        return blacklistedTokenQueryRepository.existsByToken(token);
    }
}
