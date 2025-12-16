package com.mikky.corebanking.authenticationservice.domain.exceptions;

public class UsernameAlreadyExistsException extends CustomException {

    public UsernameAlreadyExistsException(String username) {
        super("Username[" + username + "] already exists!");
    }
}
