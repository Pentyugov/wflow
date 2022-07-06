package com.pentyugov.wflow.core.service;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public interface MessageService {

    String NAME = "wflow$MessageService";

    String getMessage(String path, String key);

    String getMessage(String path, String key, Locale locale);

    String getMessageFormResourceBundle(String path, String key, Locale locale);

    Locale getAppLocale();
}
