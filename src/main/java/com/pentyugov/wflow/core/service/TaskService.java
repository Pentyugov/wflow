package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.CardHistoryDto;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.TaskSignalProcRequest;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {

    String NAME = "wflow$TaskService";

    Task createNewTask(TaskDto taskDto, Principal principal) throws UserNotFoundException, ProjectNotFoundException;

    Task saveTask(Task task);

    Task updateTask(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException;

    void deleteTask(UUID id);

    Task getTaskById(UUID id);

    List<Task> getActiveForExecutor(Principal principal) throws UserNotFoundException;

    List<Task> getProductivityData(Principal principal) throws UserNotFoundException;

    String signalProc(TaskSignalProcRequest taskSignalProcRequest, Principal principal) throws UserNotFoundException, TaskNotFoundException;

    String startTask(Task task, User currentUser);

    String cancelTask(Task task, User currentUser, String comment);

    String executeTask(Task task, User currentUser, String comment);

    String reworkTask(Task task, User currentUser, String comment);

    String finishTask(Task task, User currentUser, String comment);

    List<Task> getPriorityTasksForUser(int priority, Principal principal) throws UserNotFoundException;

    List<Task> getTasksWhereCurrentUserExecutor(int priority, Principal principal) throws UserNotFoundException;

    Page<Task> getPageForCurrentUser(Optional<Integer> page, Optional<String> sortBy, Principal principal) throws UserNotFoundException;

    List<CardHistoryDto> getTaskHistory(Task task);

    Task createTaskFromProxy(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException;

    List<Task> getAllTasks();

    List<TaskDto> getAllTaskDto(Principal principal) throws UserNotFoundException;

    TaskDto createProxyFromTask(Task task);

    String getNextTaskNumber();

    void checkOverdueTasks();



}
