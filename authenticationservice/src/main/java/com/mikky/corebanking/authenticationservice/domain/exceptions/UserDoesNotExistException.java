package com.mikky.corebanking.authenticationservice.domain.exceptions;

import com.mikky.corebanking.events.domain.exceptions.CustomException;

public class UserDoesNotExistException extends CustomException {

    public UserDoesNotExistException(String username) {
        super("User with username (" + username + ") Does not exist");
    }
}
