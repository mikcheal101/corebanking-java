package com.mikky.corebanking.authenticationservice.infrastructure.persistence.command;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mikky.corebanking.authenticationservice.domain.model.User;

@Repository
public interface UserCommandRepository extends JpaRepository<User, UUID> {
}
