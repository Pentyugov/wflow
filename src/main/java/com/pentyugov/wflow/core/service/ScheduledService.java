package com.pentyugov.wflow.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledService {

    private final TaskService taskService;
    private final EmailService emailService;

    @Autowired
    public ScheduledService(TaskService taskService, EmailService emailService) {
        this.taskService = taskService;
        this.emailService = emailService;
    }


    @Scheduled(cron = "${job.cron.resendEmail}")
    public void checkNotSentMails() {
        emailService.resendEmails();
    }


    @Scheduled(cron = "${job.cron.checkOverdue}")
    public void checkOverdueTask() {
        taskService.checkOverdueTasks();
    }


}
