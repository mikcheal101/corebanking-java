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
public class ForgotPasswordEvent implements Event {
    private EventType eventType;
    private int version;
    private Instant occurredAt;
    private Payload payload;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private String username;
        private String resetToken;
        private int expiry;
        private Instant changedAt;
        private Set<Channel> channels;
    }
}
