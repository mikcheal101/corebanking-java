package com.mikky.corebanking.authenticationservice.domain.model;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "password_reset_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(nullable = false)
    private Instant expiry = Instant.now();

    @Column(nullable = false)
    private boolean valid = true;
}
