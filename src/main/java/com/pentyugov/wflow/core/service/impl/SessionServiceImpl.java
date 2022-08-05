package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service(SessionService.NAME)
@RequiredArgsConstructor()
public class SessionServiceImpl implements SessionService {

    @Value("${telbot.api.url}/server-started")
    private String TELBOT_STARTUP_ENDPOINT;

    private final RestTemplate restTemplate;
    private Boolean connected = Boolean.FALSE;
    private String telbotSessionId = "";

    private final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Override
    public void notifyTelbotOfStartup() {
        UUID sessionId = UUID.randomUUID();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("session-id", sessionId.toString());
        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    TELBOT_STARTUP_ENDPOINT,
                    HttpMethod.POST,
                    new HttpEntity<>(null, httpHeaders),
                    Object.class,
                    Collections.emptyMap()
            );

            if (response.getStatusCode().equals(HttpStatus.OK) &&
                sessionId.toString().equals(Objects.requireNonNull(httpHeaders.get("session-id")).get(0))) {
                telbotSessionId = sessionId.toString();
                connected = Boolean.TRUE;
                logger.info("Successfully connected to Telbot...");
            }
            response.getHeaders().get("sessionId");
        } catch (HttpStatusCodeException | ResourceAccessException e) {
            logger.error("Telbot service is unavailable...");
        }

        if (!connected) {
            logger.error("Could not connect to Telbot...");
        }
    }

    @Override
    public String getTelbotSessionId() {
        return telbotSessionId;
    }
}
