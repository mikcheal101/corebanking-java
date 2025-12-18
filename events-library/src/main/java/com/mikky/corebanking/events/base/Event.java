package com.mikky.corebanking.events.base;

public interface Event {
    EventType getEventType();
    String toString();
}
