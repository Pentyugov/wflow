package com.pentyugov.wflow.core.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pentyugov.wflow.core.service.UserWsSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service(UserWsSessionService.NAME)
public class UserWsSessionServiceImpl implements UserWsSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserWsSessionServiceImpl.class);

    private final LoadingCache<String, String> userWsSessionCache;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public UserWsSessionServiceImpl(SimpMessagingTemplate messagingTemplate) {
        super();
        this.messagingTemplate = messagingTemplate;
        userWsSessionCache = CacheBuilder.newBuilder()
                .maximumSize(1000).build(new CacheLoader<>() {
                    @Override
                    public String load(String key) {
                        return "";
                    }
                });
    }

    public void addUserSessionToCache(String sessionId, String userId) {
        userWsSessionCache.put(sessionId, userId);
        onUserChangeStatus();
    }

    public void deleteUserSessionFromCache(String sessionId) {
        userWsSessionCache.put(sessionId, "");
        onUserChangeStatus();
    }

    public boolean isUserOnline(String userId) {
        return userWsSessionCache.asMap().containsValue(userId);
    }

    public boolean isUserSessionPresent(String sessionId) {
        return userWsSessionCache.asMap().containsKey(sessionId);
    }

    private void onUserChangeStatus() {
        try {
            messagingTemplate.convertAndSend("/user/change-chat-status", "");
        } catch (MessageDeliveryException e) {
            LOGGER.error(e.getMessage());
        }

    }
}
