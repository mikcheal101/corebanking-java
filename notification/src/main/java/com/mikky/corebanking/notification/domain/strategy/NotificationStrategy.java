package com.mikky.corebanking.notification.domain.strategy;

import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.notification.Channel;

public interface NotificationStrategy {
    Channel getChannel();
    boolean send(Event event);
    boolean supports(Event event);
}
