package com.mikky.corebanking.notification.infrastructure.messaging.strategy.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.domain.message.MessageType;
import com.mikky.corebanking.notification.domain.strategy.NotificationStrategy;
import com.mikky.corebanking.notification.infrastructure.messaging.sms.SmsMessage;
import com.mikky.corebanking.notification.infrastructure.persistence.query.MessageTemplateQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSignedInSmsNotificationStrategy implements NotificationStrategy {

    private final SmsMessage smsMessage;
    private final MessageTemplateQueryService messageTemplateQueryService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Channel getChannel() {
        return Channel.SMS;
    }

    @Override
    public boolean supports(Event event) {
        return event instanceof UserSignedInEvent;
    }

    @Override
    public boolean send(Event event) {
        UserSignedInEvent userSignedInEvent = (UserSignedInEvent) event;
        // pull the template message from db
        try {
            var template = this.messageTemplateQueryService
                    .getByChannelAndEventType(this.getChannel(), userSignedInEvent.getEventType());
            this.smsMessage.setBody(template.getContent());
            this.smsMessage.setFrom(template.getSender());
            this.smsMessage.setMessageType(MessageType.PLAINTEXT);
            this.smsMessage.setTo(userSignedInEvent.getPayload().getUsername());
            this.smsMessage.sendMessage();
            this.logger.info("{} Notification sent", this.getChannel());
            return true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }

    }

}
