package com.mikky.corebanking.authenticationservice.infrastructure.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.authenticationservice.application.command.service.AuthCommandService;
import com.mikky.corebanking.authenticationservice.application.query.service.AuthQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class DatabaseSeeder implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final AuthCommandService authCommandService;
    private final AuthQueryService authQueryService;

    @Override
    public void run(String... args) {
        try {

            if (this.authQueryService.countRoles() == 0) {
                logger.info("Seeding roles into the database...");

                this.authCommandService.createRole("CUSTOMER_B2C");
                this.authCommandService.createRole("CUSTOMER_B2B");
                this.authCommandService.createRole("MERCHANT");
                this.authCommandService.createRole("MERCHANT_ADMINISTRATOR");
                this.authCommandService.createRole("ADMINISTRATOR");
                this.authCommandService.createRole("SUPER_ADMINISTRATOR");

                this.authCommandService.createPermission("READ_AUDIT_LOG", "ADMINISTRATOR");

                logger.info("Seeding roles completed successfully!");
            } else {
                logger.info("Roles already exist. Skipping seeding.");
            }
        } catch (Exception e) {
            logger.error("Error occurred while seeding roles", e);
        }
    }
}