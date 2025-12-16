package com.mikky.corebanking.authenticationservice.query.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikky.corebanking.authenticationservice.domain.model.Role;
import com.mikky.corebanking.authenticationservice.domain.model.RoleType;

@Repository
public interface RoleQueryRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRoleType(RoleType roleType);
}
