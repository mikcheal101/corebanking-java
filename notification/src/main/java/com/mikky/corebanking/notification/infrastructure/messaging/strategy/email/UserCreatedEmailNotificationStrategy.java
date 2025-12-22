package com.mikky.corebanking.notification.infrastructure.messaging.strategy.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.auth.UserCreatedEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.application.query.service.MessageTemplateQueryService;
import com.mikky.corebanking.notification.domain.message.MessageType;
import com.mikky.corebanking.notification.domain.message.email.EmailMessage;
import com.mikky.corebanking.notification.domain.strategy.NotificationStrategy;
import com.mikky.corebanking.notification.infrastructure.messaging.email.DefaultEmailMessageFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCreatedEmailNotificationStrategy implements NotificationStrategy {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageTemplateQueryService messageTemplateQueryService;
    private final DefaultEmailMessageFactory emailMessageFactory;

    @Override
    public boolean supports(Event event) {
        return event instanceof UserCreatedEvent;
    }

    @Override
    public Channel getChannel() {
        return Channel.EMAIL;
    }

    @Override
    public boolean send(Event event) {
        try {
            var template = this.messageTemplateQueryService
                    .getByChannelAndEventType(this.getChannel(), event.getEventType());

            EmailMessage emailMessage = this.emailMessageFactory.getEmailMessage(template.isHtml());
            emailMessage.setFrom(template.getSender());
            emailMessage.setBody(template.getContent());
            emailMessage.setMessageType(template.isHtml() ? MessageType.HTML : MessageType.PLAINTEXT);
            emailMessage.setTo(((UserCreatedEvent) event).getPayload().getUsername());
            emailMessage.sendMessage();

            this.logger.info("{} Notification sent", this.getChannel());
            return true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }
}
