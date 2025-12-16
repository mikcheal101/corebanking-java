package com.mikky.corebanking.authenticationservice.domain.event;

import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent implements Event {
    private EventType eventType;
    private int version;
    private Instant occurredAt;
    private Payload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Payload {
        private String username;
        private int expiry;
        private Set<Channel> channels;
    }
}
