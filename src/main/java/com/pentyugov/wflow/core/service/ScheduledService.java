package com.pentyugov.wflow.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledService {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduledService.class);

    @Autowired
    private final EmailService emailService;

    public ScheduledService(EmailService emailService) {
        this.emailService = emailService;
    }


    @Scheduled(cron = "${job.cron.resendEmail}")
    public void checkNotSentMails() {
        LOGGER.info("Run \"checkNotSentMails\" method from \"ScheduledWorker\"");
        emailService.resendEmails();
    }


}
