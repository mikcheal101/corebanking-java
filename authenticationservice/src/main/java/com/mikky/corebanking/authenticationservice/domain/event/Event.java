package com.mikky.corebanking.authenticationservice.domain.event;

public interface Event {
    EventType getEventType();
    String toString();
}
