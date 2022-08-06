package com.pentyugov.wflow.application.utils;

import net.lingala.zip4j.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.File.REDIS_FOLDER;

@Component
public class InstallerUtils {

    @Value("${app.enable-redis}")
    private boolean enableRedis;
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallerUtils.class);
    private Process redisProcess;

    @PostConstruct
    public void startRedis() {
        if (enableRedis) {
            LOGGER.info("Starting redis redis");
            File redisExec = new File(REDIS_FOLDER + "/redis-server.exe");
            if (!isRedisInstalled(redisExec)) {
                installRedis();
            } else {
                startRedisServer();
            }
        }
    }

    public void stopRedis() {
        if (redisProcess != null) {
            LOGGER.info("Stopping redis server");
            redisProcess.toHandle().destroy();
            redisProcess.destroy();
        }
    }

    private void startRedisServer() {
        try {
            redisProcess = Runtime.getRuntime().exec(REDIS_FOLDER + "/redis-server.exe", null, new File(REDIS_FOLDER));
            LOGGER.info("Process started.. " + "[ " + redisProcess + " ]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void installRedis() {
        LOGGER.info("Starting redis installation...");
        if (!isRedisDirectoryExists()) {
            unzipRedis();
        }
    }

    private boolean isRedisDirectoryExists() {
        File file = new File(REDIS_FOLDER);
        if (file.isDirectory() && file.exists()) {
            return true;
        }
        LOGGER.info("Creating new redis folder");
        return file.mkdir();
    }

    private void unzipRedis() {
        File file;
        try {
            LOGGER.info("Starting unzip redis.zip");
            file = ResourceUtils.getFile("classpath:distr/redis.zip");
            try (ZipFile zipFile = new ZipFile(file)) {
                zipFile.extractAll(REDIS_FOLDER);
                startRedisServer();
            } catch (IOException e) {
                LOGGER.error("Error occurred during unzip redis");
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("redis.zip not found");
        }
    }

    private boolean isRedisInstalled(File redisExec) {
        return redisExec.exists();
    }

}
