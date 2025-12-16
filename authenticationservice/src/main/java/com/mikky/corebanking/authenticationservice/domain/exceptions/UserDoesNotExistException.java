package com.mikky.corebanking.authenticationservice.domain.exceptions;

public class UserDoesNotExistException extends CustomException {

    public UserDoesNotExistException(String username) {
        super("User with username (" + username + ") Does not exist");
    }
}
