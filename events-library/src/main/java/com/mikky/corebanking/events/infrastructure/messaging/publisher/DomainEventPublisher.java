package com.mikky.corebanking.events.infrastructure.messaging.publisher;

import com.mikky.corebanking.events.domain.event.Event;

public interface DomainEventPublisher<T extends Event> {
    void publish(T event);
}
