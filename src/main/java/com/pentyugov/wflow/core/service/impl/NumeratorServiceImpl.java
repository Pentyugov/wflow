package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.service.NumeratorService;
import com.pentyugov.wflow.core.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(NumeratorService.NAME)
public class NumeratorServiceImpl extends AbstractService implements NumeratorService {

    private final TaskService taskService;

    @Autowired
    public NumeratorServiceImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public String getNextNumber(String type) {
        if (NumeratorService.TYPE_TASK.equals(type.toUpperCase())) {
            return taskService.getNextTaskNumber();
        }
        return "";
    }
}
