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
public class UserSignedInEvent implements Event, Serializable {
    
    @Builder.Default
    private EventType eventType = EventType.USER_LOGGED_IN;
    
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
        private int expiry;
        private Set<Channel> channels;
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public Instant getOccurredAt() {
        return this.occurredAt;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public String getSource() {
        return "authentication";
    }

    @Override
    public Object getPayload() {
        return this.payload;
    }
}
