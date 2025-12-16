package com.mikky.corebanking.authenticationservice.command.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikky.corebanking.authenticationservice.domain.model.BlacklistedToken;

@Repository
public interface BlacklistedTokenCommandRepository extends JpaRepository<BlacklistedToken, UUID> {
}
