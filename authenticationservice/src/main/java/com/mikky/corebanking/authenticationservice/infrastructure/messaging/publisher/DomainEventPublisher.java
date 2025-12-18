package com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher;

import com.mikky.corebanking.events.base.Event;

public interface DomainEventPublisher<T extends Event> {
    void publish(T event);
}
