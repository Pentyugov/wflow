package com.pentyugov.wflow.core.service;

public interface ScreenService {
    String NAME = "wflow$ScreenService";

    boolean hasAccessToScreen(String screenAlias);
}
