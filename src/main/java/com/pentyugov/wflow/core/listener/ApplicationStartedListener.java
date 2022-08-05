package com.pentyugov.wflow.core.listener;

import com.pentyugov.wflow.core.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Lazy(false)
@Component
@RequiredArgsConstructor
public class ApplicationStartedListener {

    private final SessionService sessionService;

    private final Logger logger = LoggerFactory.getLogger(ApplicationStartedListener.class);

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        sessionService.notifyTelbotOfStartup();
    }
}
