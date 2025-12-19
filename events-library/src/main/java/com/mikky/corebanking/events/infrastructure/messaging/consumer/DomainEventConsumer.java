package com.mikky.corebanking.events.infrastructure.messaging.consumer;

import com.mikky.corebanking.events.domain.event.Event;

public interface DomainEventConsumer<T extends Event> {
    void consume(T event);
    String getTopic();
    Class<T> getEventClass();
}
