package com.mikky.corebanking.notification.infrastructure.messaging.strategy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.domain.exceptions.StrategyNotFoundException;
import com.mikky.corebanking.notification.domain.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationStrategyResolver {

    private final List<NotificationStrategy> strategies;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void resolveAndSend(Event event, Channel channel) {
        try {
            this.strategies.stream()
            .filter(strategy -> strategy.getChannel() == channel)
            .filter(strategy -> strategy.supports(event))
            .findFirst()
            .ifPresentOrElse(
                strategy -> strategy.send(event),
                () -> { throw new StrategyNotFoundException(channel); }
            );    
            this.logger.info("Resolving and sending to {}, via channel {}", event, channel);
        } catch (Exception e) {
            this.logger.error("Error Resolving and sending to {}, via channel {}. Message: {}", event, channel, e.getMessage());
        }
        
    }
}
