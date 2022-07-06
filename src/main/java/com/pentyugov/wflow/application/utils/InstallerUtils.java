package com.pentyugov.wflow.application.utils;

import net.lingala.zip4j.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.File.REDIS_FOLDER;

@Component
public class InstallerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstallerUtils.class);
    private Process redisProcess;

    @PostConstruct
    public void startRedis() {
        LOGGER.info("STARTING REDIS SERVER");
        File redisExec = new File(REDIS_FOLDER + "/redis-server.exe");
        if (!isRedisInstalled(redisExec)) {
            installRedis();
        } else {
            startRedisServer();
        }

    }


    public void stopRedis() {
        if (redisProcess != null) {
            LOGGER.info("STOPPING REDIS SERVER");
            redisProcess.toHandle().destroy();
            redisProcess.destroy();
        }
    }

    private void startRedisServer() {
        try {
            redisProcess = Runtime.getRuntime().exec(REDIS_FOLDER + "/redis-server.exe", null, new File(REDIS_FOLDER));
            LOGGER.info("PROCESS STARTED.. " + "[ " +redisProcess + " ]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void installRedis() {
        LOGGER.info("STARTING INSTALL REDIS");
        if (!isRedisDirectoryExists()) {
            unzipRedis();
        }
    }

    private boolean isRedisDirectoryExists() {
        File file = new File(REDIS_FOLDER);
        if (file.isDirectory() && file.exists()) {
            return true;
        }
        LOGGER.info("CREATING NEW REDIS FOLDER");
        return file.mkdir();
    }

    private void unzipRedis() {
        File file;
        try {
            LOGGER.info("STARTING UNZIP REDIS.ZIP");
            file = ResourceUtils.getFile("classpath:distr/redis.zip");
            try(ZipFile zipFile = new ZipFile(file)) {
                zipFile.extractAll(REDIS_FOLDER);
                startRedisServer();
                LOGGER.error("ERROR UNZIP REDIS");
            } catch (IOException e) {
                LOGGER.error("ERROR UNZIP REDIS");
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FILE WITH REDIS NOT FOUND");
        }
    }

    private boolean isRedisInstalled(File redisExec) {
        return redisExec.exists();
    }

}
