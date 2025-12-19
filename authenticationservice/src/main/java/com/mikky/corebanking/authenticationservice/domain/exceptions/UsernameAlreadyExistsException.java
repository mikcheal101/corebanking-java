package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.events.domain.exceptions.CustomException;

public class UsernameAlreadyExistsException extends CustomException {

    public UsernameAlreadyExistsException(String username) {
        super("Username[" + username + "] already exists!");
    }
}
