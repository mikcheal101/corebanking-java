package com.mikky.corebanking.events.domain.event;

import java.time.Instant;

public interface Event {
    EventType getEventType();
    Instant getOccurredAt();
    int getVersion();
    String getSource();
    Object getPayload();
    String toString();
}
