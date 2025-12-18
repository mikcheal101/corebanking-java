package com.mikky.corebanking.events.auth;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import com.mikky.corebanking.events.base.Event;
import com.mikky.corebanking.events.base.EventType;
import com.mikky.corebanking.events.notification.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordChangeEvent implements Event, Serializable {
    
    @Builder.Default
    private EventType eventType = EventType.FORGOT_PASSWORD_CHANGED;
    
    @Builder.Default
    private Instant occurredAt = Instant.now();
    
    @Builder.Default
    private int version = 1;
    
    private Payload payload;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload implements Serializable {
        private String username;
        private Set<Channel> channels;
        private int expiry;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }
}
