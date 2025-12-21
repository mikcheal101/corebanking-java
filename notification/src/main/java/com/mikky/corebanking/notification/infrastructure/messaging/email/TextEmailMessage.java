package com.mikky.corebanking.notification.infrastructure.messaging.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.mikky.corebanking.notification.domain.message.email.EmailMessage;

@Service
public class TextEmailMessage extends EmailMessage {

    private final SimpleMailMessage simpleMailMessage;

    public TextEmailMessage(SimpleMailMessage simpleMailMessage, JavaMailSender javaMailSender) {
        super(javaMailSender);
        this.simpleMailMessage = simpleMailMessage;
    }

    @Override
    public boolean sendMessage() {
        try {
            this.simpleMailMessage.setTo(this.getTo());
            this.simpleMailMessage.setReplyTo(this.getFrom());
            this.simpleMailMessage.setFrom(this.getFrom());
            this.simpleMailMessage.setText(this.getBody());
            this.simpleMailMessage.setSubject(this.getSubject());
            this.simpleMailMessage.setCc(this.getCc().toArray(String[]::new));
            this.simpleMailMessage.setBcc(this.getBcc().toArray(String[]::new));

            this.javaMailSender.send(this.simpleMailMessage); 
            this.logger.info("Email sent to: {}", this.getTo());
            return true;   
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }
}
