package com.mikky.corebanking.authenticationservice.infrastructure.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mikky.corebanking.authenticationservice.domain.model.Role;
import com.mikky.corebanking.authenticationservice.domain.model.RoleType;
import com.mikky.corebanking.authenticationservice.infrastructure.persistence.command.RoleCommandRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final RoleCommandRepository roleCommandRepository;

    @Override
    public void run(String... args) {
        try {
            long count = roleCommandRepository.count();
            logger.info("Checking roles in database: {} existing", count);

            if (count == 0) {
                logger.info("Seeding roles into the database...");

                seedRole("Customer B2C", RoleType.CUSTOMER);
                seedRole("Customer B2B", RoleType.CUSTOMER);
                seedRole("Merchant", RoleType.CUSTOMER);
                seedRole("Merchant Administrator", RoleType.ADMIN);
                seedRole("Administrator", RoleType.ADMIN);
                seedRole("Super Administrator", RoleType.SUPER_ADMIN);

                logger.info("Seeding roles completed successfully!");
            } else {
                logger.info("Roles already exist. Skipping seeding.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while seeding roles", e);
        }
    }

    private void seedRole(String name, RoleType roleType) {
        Role role = new Role();
        role.setName(name);
        role.setRoleType(roleType);
        roleCommandRepository.save(role);
        logger.info("Seeded role: {} ({})", name, roleType);
    }
}