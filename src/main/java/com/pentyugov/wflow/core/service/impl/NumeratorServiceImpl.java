package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.service.NumeratorService;
import com.pentyugov.wflow.core.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service(NumeratorService.NAME)
@RequiredArgsConstructor
public class NumeratorServiceImpl extends AbstractService implements NumeratorService {

    private final TaskService taskService;

    @Override
    public String getNextNumber(String type) {
        if (NumeratorService.TYPE_TASK.equals(type.toUpperCase())) {
            return taskService.getNextTaskNumber();
        }
        return "";
    }
}
