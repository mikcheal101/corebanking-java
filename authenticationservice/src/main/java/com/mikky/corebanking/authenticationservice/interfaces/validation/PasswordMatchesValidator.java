package com.mikky.corebanking.authenticationservice.interfaces.validation;

import com.mikky.corebanking.authenticationservice.application.command.dto.ForgotPasswordChangeRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements  ConstraintValidator<PasswordMatches, ForgotPasswordChangeRequest> {

    @Override
    public boolean isValid(ForgotPasswordChangeRequest request, ConstraintValidatorContext context) {
        if ((request.getPassword() == null) || (request.getRetypePassword() == null)) {
            return false;
        }

        return request.getPassword().equals(request.getRetypePassword());
    }
}
