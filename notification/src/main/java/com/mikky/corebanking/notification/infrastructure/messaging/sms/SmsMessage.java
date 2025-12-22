package com.mikky.corebanking.notification.infrastructure.messaging.sms;

import org.springframework.stereotype.Component;
import com.mikky.corebanking.notification.domain.message.Message;
import com.mikky.corebanking.notification.domain.message.SendableMessage;

@Component
public class SmsMessage extends Message implements SendableMessage {

    @Override
    public boolean sendMessage() {
        try {
            this.logger.info("Sms sent to: {}", this.getTo());
            return true;
        } catch(Exception exception) {
            this.logger.error(exception.getMessage());
            return false;
        }
    }
}
