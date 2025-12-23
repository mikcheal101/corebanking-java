package com.mikky.corebanking.notification.infrastructure.persistence.query;

import org.springframework.stereotype.Service;
import com.mikky.corebanking.events.domain.event.EventType;
import com.mikky.corebanking.events.domain.event.notification.Channel;
import com.mikky.corebanking.notification.domain.exceptions.MessageTemplateNotFoundException;
import com.mikky.corebanking.notification.domain.model.MessageTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageTemplateQueryService {

    private final MessageTemplateQueryRepository messageTemplateQueryRepository;

    public MessageTemplate getByChannelAndEventType(Channel channel, EventType eventType) throws MessageTemplateNotFoundException {
        return this.messageTemplateQueryRepository
                .findByChannelAndEventType(channel, eventType)
                .orElseThrow(MessageTemplateNotFoundException::new);
    }
}
