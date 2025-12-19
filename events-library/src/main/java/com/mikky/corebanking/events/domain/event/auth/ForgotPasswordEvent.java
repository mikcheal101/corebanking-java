package com.mikky.corebanking.events.domain.event.auth;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.notification.Channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordEvent implements Event, Serializable {

    @Builder.Default
    private EventType eventType = EventType.FORGOT_PASSWORD;
    
    @Builder.Default
    private int version = 1;
    
    @Builder.Default
    private Instant occurredAt = Instant.now();

    private Payload payload;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload implements Serializable {
        private String username;
        private String resetToken;
        private int expiry;
        private Set<Channel> channels;

        @Builder.Default
        private Instant changedAt = Instant.now();
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }
}
