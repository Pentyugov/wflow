package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;

@Service(MessageService.NAME)
public class MessageServiceImpl implements MessageService {

    @Value("${spring.web.locale}")
    private String appLocale;

    public String getMessage(String path, String key) {
        return getMessage(path, key, null);
    }

    public String getMessage(String path, String key, Locale locale) {
        return getMessageFormResourceBundle(path, key, locale);
    }

    public String getMessageFormResourceBundle(String path, String key, Locale locale) {
        if (locale == null) {
            locale = getAppLocale();
        }
        return ResourceBundle.getBundle(path, locale).getString(key);
    }

    public Locale getAppLocale() {
        return new Locale(appLocale);
    }
}
