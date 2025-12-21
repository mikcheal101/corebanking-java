package com.mikky.corebanking.notification.infrastructure.messaging.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.mikky.corebanking.notification.domain.message.email.EmailMessage;
import jakarta.mail.internet.MimeMessage;

@Service
public class HtmEmailMessage extends EmailMessage {

    private MimeMessage mimeMessage;

    public HtmEmailMessage(JavaMailSender javaMailSender) {
        super(javaMailSender);
        this.mimeMessage = this.javaMailSender.createMimeMessage();
    }

    @Override
    public boolean sendMessage() {
        try {
            var mimeMessageHelper = new MimeMessageHelper(this.mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(this.getFrom());
            mimeMessageHelper.setTo(getTo());
            mimeMessageHelper.setSubject(this.getSubject());
            mimeMessageHelper.setBcc(this.getBcc().toArray(String[]::new));
            mimeMessageHelper.setCc(this.getCc().toArray(String[]::new));
            mimeMessageHelper.setReplyTo(this.getFrom());
            mimeMessageHelper.setText(this.getBody(), true);

            this.javaMailSender.send(mimeMessage);
            this.logger.info("Html Email sent to: {}", this.getTo());
            return true;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            return false;
        }
    }

}
