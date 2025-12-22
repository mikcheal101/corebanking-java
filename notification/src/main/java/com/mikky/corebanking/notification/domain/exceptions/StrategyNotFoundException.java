package com.mikky.corebanking.notification.domain.exceptions;

import com.mikky.corebanking.events.domain.event.notification.Channel;

public class StrategyNotFoundException extends RuntimeException {

    public StrategyNotFoundException(Channel channel) {
        super(String.format("Strategy for channel: %s not found!", channel.toString()));
    }
}
