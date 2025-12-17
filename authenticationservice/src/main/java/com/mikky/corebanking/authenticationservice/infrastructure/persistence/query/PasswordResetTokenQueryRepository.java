package com.mikky.corebanking.authenticationservice.infrastructure.persistence.query;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mikky.corebanking.authenticationservice.domain.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenQueryRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(String token);
}
