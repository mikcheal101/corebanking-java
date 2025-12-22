package com.mikky.corebanking.notification;

import java.util.Set;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordEvent;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;

public class TestEventFactory {

    public static ForgotPasswordEvent forgotPasswordEvent(Set<Channel> channels) {
        var payload = ForgotPasswordEvent.Payload.builder()
                .channels(channels)
                .username("hirekaanmicheal@gmail.com")
                .expiry(3200)
                .build();

        return ForgotPasswordEvent.builder()
                .payload(payload)
                .build();
    }

    public static ForgotPasswordChangeEvent forgotPasswordChangeEvent(Set<Channel> channels) {
        var payload = ForgotPasswordChangeEvent.Payload.builder()
                .channels(channels)
                .username("mikkytrionze@yahoo.com")
                .expiry(3200)
                .build();

        return ForgotPasswordChangeEvent.builder()
                .payload(payload)
                .build();
    }

    public static UserCreatedEvent userCreatedEvent(Set<Channel> channels) {
        var payload = UserCreatedEvent.Payload.builder()
                .channels(channels)
                .expiry(3200)
                .username("+2349020464737")
                .build();

        return UserCreatedEvent.builder()
                .payload(payload)
                .build();
    }

    public static UserSignedInEvent userSignedInEvent(Set<Channel> channels) {
        var payload = UserSignedInEvent.Payload.builder()
                .channels(channels)
                .expiry(3200)
                .username("+2349020464737")
                .build();

        return UserSignedInEvent.builder()
                .payload(payload)
                .build();
    }
}
