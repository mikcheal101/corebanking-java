package com.mikky.corebanking.authenticationservice.infrastructure.messaging.publisher;

import com.mikky.corebanking.authenticationservice.domain.event.Event;

public interface DomainEventPublisher<T extends Event> {
    void publish(T event);
}
