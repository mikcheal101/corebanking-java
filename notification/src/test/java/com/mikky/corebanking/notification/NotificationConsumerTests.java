package com.mikky.corebanking.notification;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.ChangePasswordEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.ForgotPasswordEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.UserCreatedEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.consumer.auth.UserSignedInEventConsumer;
import com.mikky.corebanking.notification.infrastructure.messaging.strategy.NotificationStrategyResolver;


@ExtendWith(MockitoExtension.class)
class NotificationConsumerTests {
    
    @Mock
    private NotificationStrategyResolver notificationStrategyResolver;

    @InjectMocks
    private ChangePasswordEventConsumer changePasswordEventConsumer;

    @InjectMocks
    private ForgotPasswordEventConsumer forgotPasswordEventConsumer;

    @InjectMocks
    private UserCreatedEventConsumer userCreatedEventConsumer;

    @InjectMocks
    private UserSignedInEventConsumer userSignedInEventConsumer;

    @Test
    void shouldDispatchNotificationForChangePasswordEvent() {
        // given
        ForgotPasswordChangeEvent event = TestEventFactory.forgotPasswordChangeEvent(Set.of(Channel.EMAIL));

        // when
        changePasswordEventConsumer.consume(event);

        // then 
        verify(notificationStrategyResolver).resolveAndSend(event, Channel.EMAIL);
        verifyNoMoreInteractions(notificationStrategyResolver);
    }

    @Test
    void shouldDispatchNotificationForForgotPasswordEvent() {
        // given
        ForgotPasswordEvent event = TestEventFactory.forgotPasswordEvent(Set.of(Channel.EMAIL));

        // when 
        forgotPasswordEventConsumer.consume(event);

        // then
        verify(notificationStrategyResolver).resolveAndSend(event, Channel.EMAIL);
        verifyNoMoreInteractions(notificationStrategyResolver);
    }

    @Test
    void shouldDispatchNotificationForUserCreatedEvent() {
        // given
        UserCreatedEvent event = TestEventFactory.userCreatedEvent(Set.of(Channel.SMS));

        // when
        userCreatedEventConsumer.consume(event);

        // then
        verify(notificationStrategyResolver).resolveAndSend(event, Channel.SMS);
        verifyNoMoreInteractions(notificationStrategyResolver);
    }

    @Test
    void shouldDispatchNotificationForUserSignedInEvent() {
        // given
        UserSignedInEvent event = TestEventFactory.userSignedInEvent(Set.of(Channel.SMS));

        // when
        userSignedInEventConsumer.consume(event);

        // then
        verify(notificationStrategyResolver).resolveAndSend(event, Channel.SMS);
        verifyNoMoreInteractions(notificationStrategyResolver);
    }

    @Test
    void shouldFailWithUncreatedNotification() {
        // given
        Set<Channel> channels = Set.of(Channel.FACEBOOK, Channel.INSTAGRAM, Channel.PUSH, Channel.WHATSAPP);
        UserSignedInEvent event = TestEventFactory.userSignedInEvent(channels);
        
        // when
        this.userSignedInEventConsumer.consume(event);

        channels.forEach(channel -> {
            // then
            verify(this.notificationStrategyResolver).resolveAndSend(event, channel);
        });

        verifyNoMoreInteractions(this.notificationStrategyResolver);
    }

}
