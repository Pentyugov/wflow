package com.pentyugov.wflow.application.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ApplicationUtils {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUtils(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String getVerificationCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public String encodeString(CharSequence toEncode) {
        return passwordEncoder.encode(toEncode);
    }

    public boolean encodingStringMatches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
