package com.mikky.corebanking.events.domain.event;

public interface Event {
    EventType getEventType();
    String toString();
}
