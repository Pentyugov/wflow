package com.pentyugov.wflow.core.validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.pentyugov.wflow.core.annotation.ValidPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {


    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return (validatePhone(phoneNumber));
    }

    private boolean validatePhone(String phoneNumber)  {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        PhoneNumber number;
        try {
            number = phoneNumberUtil.parse(phoneNumber, PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            return phoneNumberUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
