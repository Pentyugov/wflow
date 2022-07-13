package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.*;
import com.pentyugov.wflow.core.dto.CardHistoryDto;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.core.repository.TaskRepository;
import com.pentyugov.wflow.core.service.*;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service(TaskService.NAME)
public class TaskServiceImpl extends AbstractService implements TaskService {

    @Value("${source.service.notification}")
    private String sourcePath;

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final WorkflowService workflowService;
    private final IssueService issueService;
    private final CalendarEventService calendarEventService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, NotificationService notificationService, WorkflowService workflowService, IssueService issueService, CalendarEventService calendarEventService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.workflowService = workflowService;
        this.issueService = issueService;
        this.calendarEventService = calendarEventService;
    }

    public Task createNewTask(TaskDto taskDto, Principal principal) throws UserNotFoundException {
        Task task = createTaskFromProxy(taskDto);
        task.setState(Task.STATE_CREATED);
        User creator = userService.getUserByPrincipal(principal);
        task.setCreator(creator);
        task.setInitiator(creator);
        return taskRepository.save(task);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(TaskDto taskDto) throws UserNotFoundException {
        Task task = createTaskFromProxy(taskDto);
        return taskRepository.save(task);
    }

    public void deleteTask(UUID id) {
        taskRepository.delete(id);
    }

    public Task getTaskById(UUID id) {
        return taskRepository.getById(id);
    }

    @Override
    public List<Task> getActiveForExecutor(Principal principal) throws UserNotFoundException {
        return taskRepository.findActiveForExecutor(userService.getUserByPrincipal(principal).getId());
    }

    public String startTask(Task task, User currentUser) {
        task.setInitiator(currentUser);
        task = workflowService.startTaskProcess(task, currentUser);
        taskRepository.save(task);
        calendarEventService.addCalendarEventForCard(task);
        User executor = task.getExecutor();
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.assigned.to.executor", task.getNumber());
        Notification notification = notificationService.createNotification(title, message, Notification.INFO, Notification.WORKFLOW, executor);
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());
        return getMessage(sourcePath, "notification.task.assigned.message", task.getNumber(), executor.getUsername());
    }

    public String cancelTask(Task task, User currentUser, String comment) {
        task = workflowService.cancelTaskProcess(task, currentUser, comment);
        taskRepository.save(task);
        User executor = task.getExecutor();
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.canceled.to.executor", task.getNumber(), currentUser.getUsername());
        Notification notification = notificationService.createNotification(title, message, Notification.WARNING, Notification.WORKFLOW, executor);
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());
        return getMessage(sourcePath, "notification.task.canceled.message", task.getNumber(), executor.getUsername());
    }

    public String executeTask(Task task, User currentUser, String comment) {
        task = workflowService.executeTask(task, currentUser, comment);
        taskRepository.save(task);
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.executed.to.initiator", task.getNumber(), currentUser.getUsername());
        Notification notification = notificationService.createNotification(title, message, Notification.SUCCESS, Notification.WORKFLOW, task.getInitiator());
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());
        return getMessage(sourcePath, "notification.task.executed.message", task.getNumber());
    }

    public String reworkTask(Task task, User currentUser, String comment) {
        task = workflowService.reworkTask(task, currentUser, comment);
        taskRepository.save(task);
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.rework.to.executor", task.getNumber(), currentUser.getUsername());
        Notification notification = notificationService.createNotification(title, message, Notification.DANGER, Notification.WORKFLOW, task.getExecutor());
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());

        return getMessage(sourcePath, "notification.task.rework.message", task.getNumber());
    }

    public String finishTask(Task task, User currentUser, String comment) {
        task = workflowService.finishTask(task, currentUser, comment);
        taskRepository.save(task);
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.finish.to.executor", task.getNumber(), currentUser.getUsername());
        Notification notification = notificationService.createNotification(title, message, Notification.SUCCESS, Notification.WORKFLOW, task.getExecutor());
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());

        return getMessage(sourcePath, "notification.task.finish.message", task.getNumber());
    }

    public List<Task> getPriorityTasksForUser(int priority, Principal principal) throws UserNotFoundException {
        return taskRepository.findAllByPriorityForUser(priority, userService.getUserByPrincipal(principal).getId());
    }

    public List<Task> getTasksWhereCurrentUserExecutor(int priority, Principal principal) throws UserNotFoundException {
        List<Card> cards = issueService.getCardsByExecutor(userService.getUserByPrincipal(principal));
        return taskRepository.findAllByPriorityWhereUserExecutor(priority, userService.getUserByPrincipal(principal).getId(), cards);
    }

    public Page<Task> getPageForCurrentUser(Optional<Integer> page, Optional<String> sortBy, Principal principal) throws UserNotFoundException {
        User currentUser = userService.getUserByPrincipal(principal);
        List<? extends Card> cards;
        if (userService.isUserAdmin(currentUser)) {
            cards = taskRepository.findAll();
        } else {
            cards = issueService.getAllCardsForUser(currentUser);
        }

        List<UUID> ids = cards.stream().map(Card::getId).collect(Collectors.toList());
        return taskRepository.findByIdInOrInitiatorId(ids, currentUser.getId(), PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC, sortBy.orElse("createDate")));
    }

    public List<CardHistoryDto> getTaskHistory(Task task) {
        List<Issue> issues = issueService.getAllIssuesByCard(task);
        List<CardHistoryDto> result = new ArrayList<>();
        issues.forEach(issue -> result.add(issueService.createCardHistoryDto(issue)));
        return result;
    }


    public Task createTaskFromProxy(TaskDto taskDto) throws UserNotFoundException {
        Task task;
        if (taskDto.getId() != null) {
            task = taskRepository.getById(taskDto.getId());
        } else {
            task = new Task();
        }
        task.setPriority(taskDto.getPriority());
        task.setNumber(taskDto.getNumber());
        task.setDescription(taskDto.getDescription());
        task.setComment(taskDto.getComment());
        task.setState(taskDto.getState());
        task.setOverdue(taskDto.getOverdue());
        task.setExecutionDatePlan(taskDto.getExecutionDatePlan());
        task.setExecutionDateFact(taskDto.getExecutionDateFact());
        task.setStarted(taskDto.getStarted());

        if (!ObjectUtils.isEmpty(taskDto.getCreator())) {
            task.setCreator(userService.getUserById(taskDto.getCreator().getId()));
        }

        if (!ObjectUtils.isEmpty(taskDto.getExecutor())) {
            task.setExecutor(userService.getUserById(taskDto.getExecutor().getId()));
        }

        if (!ObjectUtils.isEmpty(taskDto.getInitiator())) {
            task.setInitiator(userService.getUserById(taskDto.getInitiator().getId()));
        }

        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<TaskDto> getAllTaskDto(Principal principal) throws UserNotFoundException {
        User currentUser = userService.getUserByPrincipal(principal);
        if (userService.isUserAdmin(currentUser)) {
            return taskRepository.findAll()
                    .stream()
                    .map(this::createProxyFromTask)
                    .collect(Collectors.toList());
        }

        return taskRepository.findAllForUser(currentUser.getId())
                .stream()
                .map(this::createProxyFromTask)
                .collect(Collectors.toList());
    }

    public TaskDto createProxyFromTask(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setPriority(task.getPriority());
        taskDto.setNumber(task.getNumber());
        taskDto.setOverdue(task.getOverdue());
        taskDto.setDescription(task.getDescription());
        taskDto.setComment(task.getComment());
        taskDto.setState(task.getState());
        taskDto.setExecutionDatePlan(task.getExecutionDatePlan());
        taskDto.setExecutionDateFact(task.getExecutionDateFact());
        taskDto.setStarted(task.getStarted());

        long days = getDaysUntilDueDate(taskDto.getExecutionDatePlan());
        taskDto.setDaysUntilDueDate(days);

        if (!ObjectUtils.isEmpty(task.getCreator())) {
            taskDto.setCreator(userService.createUserDtoFromUser(task.getCreator()));
        }

        if (!ObjectUtils.isEmpty(task.getExecutor())) {
            taskDto.setExecutor(userService.createUserDtoFromUser(task.getExecutor()));
        }

        if (!ObjectUtils.isEmpty(task.getInitiator())) {
            taskDto.setInitiator(userService.createUserDtoFromUser(task.getInitiator()));
        }

        return taskDto;
    }

    private long getDaysUntilDueDate(Date executionDatePlan) {
        if (ObjectUtils.isEmpty(executionDatePlan))
            return 0L;
        LocalDateTime in = LocalDateTime.ofInstant(executionDatePlan.toInstant(), ZoneId.systemDefault());
        return Duration.between(LocalDateTime.now(), in).toDays();

    }

}
