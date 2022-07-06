package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;

public interface WorkflowService {

    String NAME = "wflow$WorkflowService";

    Task startTaskProcess(Task task, User currentUser);

    Task cancelTaskProcess(Task task, User currentUser, String comment);

    Task executeTask(Task task, User currentUser, String comment);

    Task reworkTask(Task task, User currentUser, String comment);

    Task finishTask(Task task, User currentUser, String comment);


}
