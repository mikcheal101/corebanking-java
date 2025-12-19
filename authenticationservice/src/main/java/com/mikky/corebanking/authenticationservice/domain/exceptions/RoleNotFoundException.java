package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.authenticationservice.domain.model.RoleType;
import com.mikky.corebanking.events.domain.exceptions.CustomException;

public class RoleNotFoundException extends CustomException {
    public RoleNotFoundException(RoleType roleType) {
        super("Role-Type [" + roleType + "] not found!");
    }
}
