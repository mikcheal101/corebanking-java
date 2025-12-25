package com.mikky.corebanking.authenticationservice.domain.model;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserPermissionId {
    private UUID permissionId;
    private UUID userId;
}
