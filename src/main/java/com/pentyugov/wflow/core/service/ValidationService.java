package com.pentyugov.wflow.core.service;

public interface ValidationService {

    String NAME = "wflow$ValidationService";

    boolean isEmailValid(String email);

    String parsePhoneNumber(String phoneNumber);

}
