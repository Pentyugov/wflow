package com.pentyugov.wflow.core.service;

public interface SessionService {
    
    String NAME = "wflow$SessionService";
    
    void notifyTelbotOfStartup();

    String getTelbotSessionId();

}
