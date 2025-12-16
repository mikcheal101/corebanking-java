package com.mikky.corebanking.authenticationservice.validation;

import com.mikky.corebanking.authenticationservice.command.dto.ForgotPasswordChangeRequest;

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
