package com.mikky.corebanking.notification.infrastructure.messaging.strategy.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.auth.ForgotPasswordChangeEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.domain.message.MessageType;
import com.mikky.corebanking.notification.domain.message.email.EmailMessage;
import com.mikky.corebanking.notification.domain.model.MessageTemplate;
import com.mikky.corebanking.notification.domain.strategy.NotificationStrategy;
import com.mikky.corebanking.notification.infrastructure.messaging.email.DefaultEmailMessageFactory;
import com.mikky.corebanking.notification.infrastructure.persistence.query.MessageTemplateQueryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangePasswordEmailNotificationStrategy implements NotificationStrategy {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageTemplateQueryService messageTemplateQueryService;
    private final DefaultEmailMessageFactory emailMessageFactory;

    @Override
    public boolean supports(Event event) {
        return event instanceof ForgotPasswordChangeEvent;
    }

    @Override
    public Channel getChannel() {
        return Channel.EMAIL;
    }

    @Override
    public boolean send(Event event) {
        try {
            MessageTemplate messageTemplate = this.messageTemplateQueryService
                    .getByChannelAndEventType(this.getChannel(), event.getEventType());
            
            EmailMessage emailMessage = this.emailMessageFactory.getEmailMessage(messageTemplate.isHtml());
            emailMessage.setBody(messageTemplate.getContent());
            emailMessage.setSubject(messageTemplate.getSubject());
            emailMessage.setFrom(messageTemplate.getSender());
            emailMessage.setMessageType(messageTemplate.isHtml() ? MessageType.HTML : MessageType.PLAINTEXT);
            emailMessage.setTo(((ForgotPasswordChangeEvent)event).getPayload().getUsername());
            emailMessage.sendMessage();
            
            this.logger.info("{} Notification sent!", this.getChannel());
            return true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }

}
