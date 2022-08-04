package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.CardHistoryDto;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import com.pentyugov.wflow.web.payload.request.KanbanRequest;
import com.pentyugov.wflow.web.payload.request.TaskSignalProcRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface TaskService {

    String NAME = "wflow$TaskService";

    Task createNewTask(TaskDto taskDto) throws ProjectNotFoundException, UserNotFoundException;

    Task updateTask(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException;

    void deleteTask(UUID id) throws TaskNotFoundException;

    Task getTaskById(UUID id) throws TaskNotFoundException;

    List<Task> getActiveForExecutor();

    List<Task> getProductivityData();

    List<Task> getTasksWithFilters(FiltersRequest filtersRequest) throws UserNotFoundException;

    String signalProc(TaskSignalProcRequest taskSignalProcRequest) throws TaskNotFoundException;

    List<CardHistoryDto> getTaskHistory(Task task);

    Task createTaskFromDto(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException;

    List<Task> getAllTasks();

    List<TaskDto> getAllTaskDto();

    Page<Task> getTaskPageForTelBot(Long telUserId, Pageable pageable) throws UserNotFoundException;

    TaskDto createDto(Task task);

    TelegramTaskDto createTelegramDto(Task task);

    String getNextTaskNumber();

    void changeKanbanState(KanbanRequest[] kanbanRequest) throws TaskNotFoundException;

    void checkOverdueTasks();

}
