package com.mikky.corebanking.authenticationservice.domain.model;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissions {

    @EmbeddedId
    private UserPermissionId id;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    protected Instant createdAt = Instant.now();

    @Column(nullable = true)
    protected Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permissions;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = false)
    private PermissionEffect effect = PermissionEffect.DENY;
}
