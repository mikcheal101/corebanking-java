package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.authenticationservice.domain.model.RoleType;

public class RoleNotFoundException extends CustomException {
    public RoleNotFoundException(RoleType roleType) {
        super("Role-Type [" + roleType + "] not found!");
    }
}
