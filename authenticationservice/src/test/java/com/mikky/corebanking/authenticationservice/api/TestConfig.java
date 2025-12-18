package com.mikky.corebanking.authenticationservice.api;

import org.mockito.Mockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher.ForgotPasswordChangeEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher.ForgotPasswordEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher.UserCreatedEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher.UserSignedInEventPublisher;
import com.mikky.corebanking.authenticationservice.infrastructure.security.SecurityProperties;

@TestConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class TestConfig {

    @Bean
    public UserCreatedEventPublisher userCreatedEventPublisher() {
        return Mockito.mock(UserCreatedEventPublisher.class);
    }

    @Bean
    public UserSignedInEventPublisher userSignedInEventPublisher() {
        return Mockito.mock(UserSignedInEventPublisher.class);
    }

    @Bean
    public ForgotPasswordEventPublisher forgotPasswordEventPublisher() {
        return Mockito.mock(ForgotPasswordEventPublisher.class);
    }

    @Bean
    public ForgotPasswordChangeEventPublisher forgotPasswordChangeEventPublisher() {
        return Mockito.mock(ForgotPasswordChangeEventPublisher.class);
    }
}
