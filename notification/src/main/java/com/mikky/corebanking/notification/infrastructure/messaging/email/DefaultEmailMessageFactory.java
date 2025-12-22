package com.mikky.corebanking.notification.infrastructure.messaging.email;

import java.util.List;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.notification.domain.exceptions.EmailMessageNotFoundException;
import com.mikky.corebanking.notification.domain.message.email.EmailMessage;
import com.mikky.corebanking.notification.domain.message.email.EmailMessageFactory;

@Component
public class DefaultEmailMessageFactory implements EmailMessageFactory {

    private List<EmailMessage> emailMessages;

    @Override
    public EmailMessage getEmailMessage(boolean isHtml) {
        return emailMessages.stream()
            .filter(emailMessage -> this.supports(emailMessage, isHtml))
            .findFirst()
            .orElseThrow(EmailMessageNotFoundException::new);
    }

    private boolean supports(EmailMessage emailMessage, boolean supportsHtml) {
        return supportsHtml && emailMessage instanceof HtmEmailMessage;
    }
}
