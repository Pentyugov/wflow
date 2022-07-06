package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.application.utils.PersistenceManager;
import com.pentyugov.wflow.core.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

public abstract class AbstractService {

    @Value("${source.service.auth}")
    private String sourcePath;

    protected PersistenceManager persistenceManager;
    protected MessageService messageService;

    @Autowired
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    protected String getMessage(String sourcePath, String key, Object... formats) {
        String result = messageService.getMessage(sourcePath, key, null);
        if (ObjectUtils.isEmpty(formats)) {
            return result;
        }
        return String.format(result, formats);
    }

    protected String getMessage(String key, Object... formats) {
        String result = messageService.getMessage(this.sourcePath, key, null);
        if (ObjectUtils.isEmpty(formats)) {
            return result;
        }
        return String.format(result, formats);
    }
}

