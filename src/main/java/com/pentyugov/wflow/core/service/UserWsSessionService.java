package com.pentyugov.wflow.core.service;

public interface UserWsSessionService {

    String NAME = "wflow$UserWsSessionService";

    void addUserSessionToCache(String sessionId, String userId);

    void deleteUserSessionFromCache(String sessionId);

    boolean isUserOnline(String userId);

    boolean isUserSessionPresent(String sessionId);

}
