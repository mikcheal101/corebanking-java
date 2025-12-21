package com.mikky.corebanking.authenticationservice.infrastructure.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.authenticationservice.domain.model.Role;
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

                seedRole("CUSTOMER_B2C");
                seedRole("CUSTOMER_B2B");
                seedRole("MERCHANT");
                seedRole("MERCHANT_ADMINISTRATOR");
                seedRole("ADMINISTRATOR");
                seedRole("SUPER_ADMINISTRATOR");

                logger.info("Seeding roles completed successfully!");
            } else {
                logger.info("Roles already exist. Skipping seeding.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while seeding roles", e);
        }
    }

    private void seedRole(String name) {
        var role = Role.builder()
            .name(name)
            .build();
        roleCommandRepository.save(role);
        logger.info("Seeded role: {}", name);
    }
}