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
public class ForgotPasswordChangeEvent implements Event {
    
    @Builder.Default
    private EventType eventType = EventType.FORGOT_PASSWORD_CHANGED;
    
    @Builder.Default
    private Instant occurredAt = Instant.now();
    
    private int version;
    private Payload payload;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private String username;
        private Set<Channel> channels;
        private int expiry;
    }
}
