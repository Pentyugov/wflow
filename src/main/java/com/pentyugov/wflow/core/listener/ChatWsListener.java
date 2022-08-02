package com.pentyugov.wflow.core.listener;

import com.pentyugov.wflow.core.service.UserWsSessionService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.Map;

@Component
public class ChatWsListener {

    private final UserWsSessionService userWsSessionService;

    public ChatWsListener(UserWsSessionService userWsSessionService) {
        this.userWsSessionService = userWsSessionService;
    }

    @EventListener
    @SuppressWarnings("unchecked")
    public void onUserConnect(SessionConnectEvent event) {

        Map<Object, Object> map = (Map<Object, Object>) event.getMessage().getHeaders().get("nativeHeaders");
        if (map != null && map.containsKey("userId") && map.get("userId") != null) {
            String userId = (String) ((ArrayList<Object>) map.get("userId")).get(0);
            String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
            userWsSessionService.addUserSessionToCache(sessionId, userId);
        }
    }

    @EventListener
    public void onUserDisconnect(SessionDisconnectEvent event) {
        if (userWsSessionService.isUserSessionPresent(event.getSessionId())) {
            userWsSessionService.deleteUserSessionFromCache(event.getSessionId());
        }
    }
}
