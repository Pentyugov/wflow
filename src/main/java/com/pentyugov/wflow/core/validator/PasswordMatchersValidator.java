package com.pentyugov.wflow.core.validator;

import com.pentyugov.wflow.core.annotation.PasswordMatchers;
import com.pentyugov.wflow.web.payload.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchersValidator implements ConstraintValidator<PasswordMatchers, Object> {

    @Override
    public void initialize(PasswordMatchers constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SignUpRequest userSignUpRequest = (SignUpRequest) o;
        return userSignUpRequest.getPassword().equals(userSignUpRequest.getConfirmPassword());
    }
}
