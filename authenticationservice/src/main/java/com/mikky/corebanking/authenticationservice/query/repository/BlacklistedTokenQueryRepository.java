package com.mikky.corebanking.authenticationservice.query.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikky.corebanking.authenticationservice.domain.model.BlacklistedToken;

@Repository
public interface BlacklistedTokenQueryRepository extends JpaRepository<BlacklistedToken, UUID> {
    boolean existsByToken(String token);
}
