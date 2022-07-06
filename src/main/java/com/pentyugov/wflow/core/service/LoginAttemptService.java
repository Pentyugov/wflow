package com.pentyugov.wflow.core.service;

public interface LoginAttemptService {

    String NAME = "wflow$LoginAttemptService";

    void evictUserFromLoginAttemptCache(String username);

    void addUserToLoginAttemptCache(String username);

    boolean hasExceededMaxAttempts(String username);

}
