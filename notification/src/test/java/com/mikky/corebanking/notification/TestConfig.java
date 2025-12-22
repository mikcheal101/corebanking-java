package com.mikky.corebanking.notification;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.ChangePasswordEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.ForgotPasswordEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.UserCreatedEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.UserSignedInEventConsumer;


@TestConfiguration
public class TestConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        return Mockito.mock(SimpleMailMessage.class);
    }

    @Bean
    public ForgotPasswordEventConsumer forgotPasswordEventConsumer() {
        var mock = Mockito.mock(ForgotPasswordEventConsumer.class);

        Mockito.when(mock.getTopic())
            .thenReturn(EventType.FORGOT_PASSWORD.getTopic());

        return mock;
    }

    @Bean
    public ChangePasswordEventConsumer changePasswordEventConsumer() {
        var mock = Mockito.mock(ChangePasswordEventConsumer.class);

        Mockito.when(mock.getTopic())
            .thenReturn(EventType.FORGOT_PASSWORD_CHANGED.getTopic());

        return mock;
    }

    @Bean
    public UserSignedInEventConsumer userSignedInEventConsumer() {
        var mock = Mockito.mock(UserSignedInEventConsumer.class);

        Mockito.when(mock.getTopic())
            .thenReturn(EventType.USER_LOGGED_IN.getTopic());

        return mock;
    }


    @Bean
    public UserCreatedEventConsumer userCreatedEventConsumer() {
        var mock = Mockito.mock(UserCreatedEventConsumer.class);

        Mockito.when(mock.getTopic())
            .thenReturn(EventType.USER_CREATED.getTopic());

        return mock;
    }
}
