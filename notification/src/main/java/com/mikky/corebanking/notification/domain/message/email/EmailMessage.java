package com.mikky.corebanking.notification.domain.message.email;

import java.util.List;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import com.mikky.corebanking.notification.domain.message.Message;
import com.mikky.corebanking.notification.domain.message.SendableMessage;
import lombok.Getter;

@Getter
@Component
public abstract class EmailMessage extends Message implements SendableMessage {

    private String subject;
    private List<String> cc;
    private List<String> bcc;
    protected final JavaMailSender javaMailSender;

    protected EmailMessage(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void setCc(String... cc) {
        if (cc.length > 0) {
            this.cc = List.of(cc);
        }
    }

    public void setBcc(String... bcc) {
        if (bcc.length > 0) {
            this.bcc = List.of(bcc);
        }
    }

    public Message setSubject(String subject) throws IllegalArgumentException {
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject is required!");
        }
        return this;
    }
}
