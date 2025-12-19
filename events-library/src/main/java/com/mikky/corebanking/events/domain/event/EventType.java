package com.mikky.corebanking.events.domain.event;

import lombok.Getter;

@Getter
public enum EventType {

    // authentication events
    USER_CREATED("auth.user-created"),
    USER_LOGGED_IN("auth.user-logged-in"),
    FORGOT_PASSWORD_CHANGED("auth.forgot-password-changed"),
    FORGOT_PASSWORD("auth.forgot-password"),
    
    // notification events
    NOTIFICATION_SENT("notification.notification-sent");

    private final String topic;

    EventType(String topic) {
        this.topic = topic;
    }
}
