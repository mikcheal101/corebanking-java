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
    private EventType eventType;
    private Instant occuredAt = Instant.now();
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
