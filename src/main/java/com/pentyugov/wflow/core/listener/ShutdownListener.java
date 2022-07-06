package com.pentyugov.wflow.core.listener;

import com.pentyugov.wflow.application.utils.InstallerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ShutdownListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownListener.class);

    private final InstallerUtils installerUtils;

    @Autowired
    public ShutdownListener(InstallerUtils installerUtils) {
        this.installerUtils = installerUtils;
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        LOGGER.warn("EXECUTING PREDESTROY CONFIGURATION");
        installerUtils.stopRedis();
        System.out.println("Spring Container is destroyed!");
    }
}
