package com.mikky.corebanking.authenticationservice.messaging.publisher;

import com.mikky.corebanking.authenticationservice.domain.event.Event;

public abstract class EventPublisher {
    abstract void publish(Event event) throws RuntimeException;
}
