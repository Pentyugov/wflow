package com.pentyugov.wflow.application.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class ApplicationProperties {

    private boolean saveImageInDb;

    public boolean isSaveImageInDb() {
        return saveImageInDb;
    }

    public void setSaveImageInDb(boolean saveImageInDb) {
        this.saveImageInDb = saveImageInDb;
    }
}
