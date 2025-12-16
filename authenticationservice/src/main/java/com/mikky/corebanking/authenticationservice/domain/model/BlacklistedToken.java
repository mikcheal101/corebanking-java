package com.mikky.corebanking.authenticationservice.domain.model;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "jwt_blacklist")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiry;
}
