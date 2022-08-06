package com.pentyugov.wflow.core.service.impl;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.pentyugov.wflow.core.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(ValidationService.NAME)
public class ValidationServiceImpl implements ValidationService {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    public boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String parsePhoneNumber(String phoneNumber) {
        phoneNumber = StringUtils.trimAllWhitespace(phoneNumber);

        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber number;
        StringBuilder builder = new StringBuilder();
        try {
            number = phoneNumberUtil.parse(phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            if (phoneNumberUtil.isValidNumber(number)) {
                builder.append(number.getCountryCode())
                       .append(number.getNationalNumber());
                return builder.toString();
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
