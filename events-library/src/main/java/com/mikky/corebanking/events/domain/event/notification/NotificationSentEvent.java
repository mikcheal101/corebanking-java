package com.mikky.corebanking.events.domain.event.notification;

import java.io.Serializable;
import java.time.Instant;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSentEvent implements Event, Serializable {

    @Builder.Default
    private EventType eventType = EventType.NOTIFICATION_SENT;

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
        private Channel channel;
        private String message;
        private boolean success;
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
        return "notification";
    }

    @Override
    public Object getPayload() {
        return this.payload;
    }
}
