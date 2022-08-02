package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.CardHistoryDto;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import com.pentyugov.wflow.web.payload.request.KanbanRequest;
import com.pentyugov.wflow.web.payload.request.TaskSignalProcRequest;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface TaskService {

    String NAME = "wflow$TaskService";

    Task createNewTask(TaskDto taskDto, Principal principal) throws UserNotFoundException, ProjectNotFoundException;

    Task updateTask(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException;

    void deleteTask(UUID id, Principal principal) throws TaskNotFoundException, UserNotFoundException;

    Task getTaskById(UUID id) throws TaskNotFoundException;

    List<Task> getActiveForExecutor(Principal principal) throws UserNotFoundException;

    List<Task> getProductivityData(Principal principal) throws UserNotFoundException;

    List<Task> getTasksWithFilters(Principal principal, FiltersRequest filtersRequest) throws UserNotFoundException;

    String signalProc(TaskSignalProcRequest taskSignalProcRequest, Principal principal) throws UserNotFoundException, TaskNotFoundException;

    String startTask(Task task, User currentUser);

    String cancelTask(Task task, User currentUser, String comment);

    String executeTask(Task task, User currentUser, String comment);

    String reworkTask(Task task, User currentUser, String comment);

    String finishTask(Task task, User currentUser, String comment);

    List<CardHistoryDto> getTaskHistory(Task task);

    Task createTaskFromDto(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException;

    List<Task> getAllTasks();

    List<TaskDto> getAllTaskDto(Principal principal) throws UserNotFoundException;

    TaskDto createDto(Task task);

    String getNextTaskNumber();

    void changeKanbanState(KanbanRequest[] kanbanRequest) throws TaskNotFoundException;

    void checkOverdueTasks();

}
