package com.mikky.corebanking.notification.domain.message.email;

public interface EmailMessageFactory {
    EmailMessage getEmailMessage(boolean isHtml);
}
