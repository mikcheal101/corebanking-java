package com.mikky.corebanking.authenticationservice.infrastructure.persistence.query;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mikky.corebanking.authenticationservice.domain.model.User;

@Repository
public interface UserQueryRepository extends JpaRepository<User, UUID> {
    public int countByUsername(String username);
    public Optional<User> findByUsername(String username);
    public boolean existsByUsername(String username);
}
