package com.mikky.corebanking.authenticationservice.command.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.mikky.corebanking.authenticationservice.command.repository.BlacklistedTokenCommandRepository;
import com.mikky.corebanking.authenticationservice.domain.model.BlacklistedToken;
import com.mikky.corebanking.authenticationservice.query.repository.BlacklistedTokenQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlacklistCommandService {

    private final BlacklistedTokenQueryRepository blacklistedTokenQueryRepository;
    private final BlacklistedTokenCommandRepository blacklistedTokenCommandRepository;

    @Transactional
    public void blackListToken(String token, Instant expiryTime) {
        var blackListedToken = new BlacklistedToken();
        blackListedToken.setToken(token);
        blackListedToken.setExpiry(expiryTime);
        blacklistedTokenCommandRepository.save(blackListedToken);        
    }

    public boolean isBlackListed(String token) {
        return blacklistedTokenQueryRepository.existsByToken(token);
    }
}
