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
public class UserCreatedEvent implements Event, Serializable {

    @Builder.Default
    private EventType eventType = EventType.USER_CREATED;

    @Builder.Default
    private int version = 1;

    @Builder.Default
    private Instant occurredAt = Instant.now();
    
    private Payload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Payload implements Serializable {
        private String username;
        private int expiry;
        private Set<Channel> channels;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }
}
