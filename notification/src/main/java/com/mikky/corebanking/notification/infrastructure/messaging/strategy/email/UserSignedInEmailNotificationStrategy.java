package com.mikky.corebanking.notification.infrastructure.messaging.strategy.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.events.domain.event.Event;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.auth.UserSignedInEvent;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.events.domain.event.notification.NotificationSentEvent;
import com.mikky.corebanking.notification.domain.message.MessageType;
import com.mikky.corebanking.notification.domain.message.email.EmailMessage;
import com.mikky.corebanking.notification.domain.strategy.NotificationStrategy;
import com.mikky.corebanking.notification.infrastructure.messaging.email.DefaultEmailMessageFactory;
import com.mikky.corebanking.notification.infrastructure.messaging.publisher.NotificationSentEventPublisher;
import com.mikky.corebanking.notification.infrastructure.persistence.query.MessageTemplateQueryService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSignedInEmailNotificationStrategy implements NotificationStrategy {

    private final DefaultEmailMessageFactory emailMessageFactory;
    private final MessageTemplateQueryService messageTemplateQueryService;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final NotificationSentEventPublisher notificationSentEventPublisher;

    @Override
    public Channel getChannel() {
        return Channel.EMAIL;
    }

    @Override
    public boolean supports(Event event) {
        return event instanceof UserSignedInEvent;
    }

    @Override
    public boolean send(Event event) {
        boolean messageSent = false;
        EmailMessage emailMessage = null;
        String recipient = ((UserSignedInEvent.Payload) event.getPayload()).getUsername();
        // get the message template and populate the content.
        try {
            var template = this.messageTemplateQueryService
                    .getByChannelAndEventType(this.getChannel(), event.getEventType());

            emailMessage = this.emailMessageFactory.getEmailMessage(template.isHtml());
            emailMessage.setFrom(template.getSender());
            emailMessage.setBody(template.getContent());
            emailMessage.setMessageType(template.isHtml() ? MessageType.HTML : MessageType.PLAINTEXT);
            emailMessage.setTo(recipient);
            emailMessage.sendMessage();

            this.logger.info("{} Notification sent", this.getChannel());
            messageSent = true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }

        try {
            NotificationSentEvent notificationEvent = NotificationSentEvent.builder()
                    .eventType(EventType.NOTIFICATION_SENT)
                    .payload(
                            NotificationSentEvent.Payload.builder()
                                    .channel(this.getChannel())
                                    .message(messageSent ? emailMessage.toString() : null)
                                    .success(messageSent)
                                    .username(recipient)
                                    .build())
                    .build();
            this.notificationSentEventPublisher.publish(notificationEvent);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }

        return messageSent;
    }
}
