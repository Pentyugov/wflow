package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Issue;
import com.pentyugov.wflow.core.domain.entity.Notification;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.CardHistoryDto;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import com.pentyugov.wflow.core.repository.TaskRepository;
import com.pentyugov.wflow.core.service.*;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import com.pentyugov.wflow.web.payload.request.KanbanRequest;
import com.pentyugov.wflow.web.payload.request.TaskSignalProcRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service(TaskService.NAME)
@RequiredArgsConstructor
public class TaskServiceImpl extends AbstractService implements TaskService {

    private static final String TASK_PREFIX = "TS-";

    @Value("${source.service.notification}")
    private String sourcePath;

    @PersistenceContext
    private EntityManager entityManager;

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final WorkflowService workflowService;
    private final IssueService issueService;
    private final CalendarEventService calendarEventService;
    private final ProjectService projectService;
    private final FilterService filterService;
    private final TelegramService telegramService;
    private final UserSessionService userSessionService;


    @Override
    public Task createNewTask(TaskDto taskDto) throws ProjectNotFoundException, UserNotFoundException {
        Task task = createTaskFromDto(taskDto);
        task.setState(Task.STATE_CREATED);
        User creator = userSessionService.getCurrentUser();
        task.setCreator(creator);
        task.setInitiator(creator);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException {
        Task task = createTaskFromDto(taskDto);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID id) throws TaskNotFoundException {
        Task toDelete = getTaskById(id);
        calendarEventService.deleteCalendarEventByCard(toDelete);
        cancelTask(toDelete, userSessionService.getCurrentUser(), "Deleted");
        taskRepository.delete(id);
    }

    @Override
    public Task getTaskById(UUID id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException(getMessage(sourcePath, "exception.task.with.id.not.found", id)));
    }

    @Override
    public List<Task> getActiveForExecutor() {
        return getActiveForExecutor(userSessionService.getCurrentUser());
    }

    @Override
    public List<Task> getProductivityData() {
        List<Task> result = new ArrayList<>();
        issueService.findCardByExecutorIdAndResult(userSessionService.getCurrentUser(), Task.STATE_ASSIGNED)
                .forEach(card -> {
                    if (card instanceof Task) {
                        result.add((Task) card);
                    }
                });
        return result;
    }

    @Override
    public List<Task> getTasksWithFilters(FiltersRequest filtersRequest) {
        List<UUID> availableTasksIds;
        if (!CollectionUtils.isEmpty(filtersRequest.getIds())) {
            availableTasksIds = filtersRequest.getIds();
        } else {
            availableTasksIds = getAllTasks(userSessionService.getCurrentUser())
                    .stream()
                    .map(Task::getId).collect(Collectors.toList());
        }

        return getTasksWithFilters(filtersRequest, availableTasksIds);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<Task> getTasksWithFilters(FiltersRequest filtersRequest, List<UUID> availableTasksIds) {
        String queryString = filterService.buildQueryString(Task.class, filtersRequest);
        Query query = entityManager.createQuery(queryString);
        query.setParameter("ids", availableTasksIds);

        for (FiltersRequest.Filter filter : filtersRequest.getFilters()) {
            if (StringUtils.hasText(filter.getProperty()) && StringUtils.hasText((filter.getCondition()))) {
                List<String> conditions = Arrays.asList(filter.getCondition().split(";"));

                if (queryString.contains(":" + filter.getProperty())) {
                    if (queryString.contains(filter.getProperty() + ".id")) {
                        query.setParameter(
                                filter.getProperty(),
                                conditions
                                        .stream()
                                        .map(UUID::fromString)
                                        .collect(Collectors.toList())
                        );
                    } else {
                        query.setParameter(filter.getProperty(), conditions);
                    }
                }
            }
        }
        return (List<Task>) query.getResultList();
    }

    @Override
    public String signalProc(TaskSignalProcRequest taskSignalProcRequest) throws TaskNotFoundException {
        User currentUser = userSessionService.getCurrentUser();
        UUID taskId = UUID.fromString(taskSignalProcRequest.getTaskId());
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new TaskNotFoundException(getMessage(sourcePath, "exception.task.with.id.not.found", taskId)));
        String action = taskSignalProcRequest.getAction().toUpperCase();

        switch (action) {
            case Task.ACTION_START:
                return startTask(task, currentUser);
            case Task.ACTION_FINISH:
                return finishTask(task, currentUser, taskSignalProcRequest.getComment());
            case Task.ACTION_EXECUTE:
                return executeTask(task, currentUser, taskSignalProcRequest.getComment());
            case Task.ACTION_REWORK:
                return reworkTask(task, currentUser, taskSignalProcRequest.getComment());
            case Task.ACTION_CANCEL:
                return cancelTask(task, currentUser, taskSignalProcRequest.getComment());
        }

        return null;
    }

    public String startTask(Task task, User currentUser) {
        task.setInitiator(currentUser);
        task.setKanbanState(Task.KANBAN_STATE_NEW);
        task = workflowService.startTaskProcess(task, currentUser);
        taskRepository.save(task);
        calendarEventService.addCalendarEventForCard(task);
        User executor = task.getExecutor();
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.assigned.to.executor", task.getNumber());
        Notification notification = notificationService.createNotification(title, message, Notification.INFO, Notification.WORKFLOW, executor, task);
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());

        if (executor.getTelLogged() && executor.getTelUserId() != null && executor.getTelChatId() != null) {
            telegramService.sendAssignedTaskMessage(executor, task);
        }
        return getMessage(sourcePath, "notification.task.assigned.message", task.getNumber(), executor.getUsername());
    }

    public String cancelTask(Task task, User currentUser, String comment) {
        task = workflowService.cancelTaskProcess(task, currentUser, comment);
        taskRepository.save(task);
        calendarEventService.deleteCalendarEventByCard(task);
        User executor = task.getExecutor();
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.canceled.to.executor", task.getNumber(), currentUser.getUsername());
        Notification notification = notificationService.createNotification(title, message, Notification.WARNING, Notification.WORKFLOW, executor, task);
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());
        return getMessage(sourcePath, "notification.task.canceled.message", task.getNumber(), executor.getUsername());
    }

    public String executeTask(Task task, User currentUser, String comment) {
        task = workflowService.executeTask(task, currentUser, comment);
        task.setKanbanState(Task.KANBAN_STATE_COMPLETED);
        taskRepository.save(task);
        calendarEventService.deleteCalendarEventByCard(task);
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.executed.to.initiator", task.getNumber(), currentUser.getUsername());
        Notification notification = notificationService.createNotification(title, message, Notification.SUCCESS, Notification.WORKFLOW, task.getInitiator(), task);
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());
        return getMessage(sourcePath, "notification.task.executed.message", task.getNumber());
    }

    public String reworkTask(Task task, User currentUser, String comment) {
        task = workflowService.reworkTask(task, currentUser, comment);
        task.setKanbanState(Task.KANBAN_STATE_NEW);
        taskRepository.save(task);
        calendarEventService.addCalendarEventForCard(task);
        String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
        String message = getMessage(sourcePath, "notification.task.rework.to.executor", task.getNumber(), currentUser.getUsername());
        Notification notification = notificationService.createNotification(title, message, Notification.DANGER, Notification.WORKFLOW, task.getExecutor(), task);
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
        Notification notification = notificationService.createNotification(title, message, Notification.SUCCESS, Notification.WORKFLOW, task.getExecutor(), task);
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService
                .createNotificationDtoFromNotification(notification), notification.getReceiver().getId());

        return getMessage(sourcePath, "notification.task.finish.message", task.getNumber());
    }

    @Override
    public List<CardHistoryDto> getTaskHistory(Task task) {
        List<Issue> issues = issueService.getAllIssuesByCard(task);
        List<CardHistoryDto> result = new ArrayList<>();
        issues.forEach(issue -> result.add(issueService.createCardHistoryDto(issue)));
        return result;
    }

    @Override
    public Task createTaskFromDto(TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException {
        Task task = taskRepository.findById(taskDto.getId()).orElse(new Task());

        if (!ObjectUtils.isEmpty(taskDto.getProject())) {
            task.setProject(projectService.getProjectById(taskDto.getProject().getId()));
        } else {
            task.setProject(null);
        }

        if (!ObjectUtils.isEmpty(taskDto.getCreator())) {
            task.setCreator(userService.getUserById(taskDto.getCreator().getId()));
        }

        if (!ObjectUtils.isEmpty(taskDto.getExecutor())) {
            task.setExecutor(userService.getUserById(taskDto.getExecutor().getId()));
        } else {
            task.setExecutor(null);
        }

        if (!ObjectUtils.isEmpty(taskDto.getInitiator())) {
            task.setInitiator(userService.getUserById(taskDto.getInitiator().getId()));
        }

        task.setPriority(taskDto.getPriority());
        task.setNumber(taskDto.getNumber());
        task.setDescription(taskDto.getDescription());
        task.setComment(taskDto.getComment());
        task.setState(taskDto.getState());
        task.setKanbanState(taskDto.getKanbanState());
        task.setKanbanOrder(taskDto.getKanbanOrder());
        task.setOverdue(taskDto.getOverdue());
        task.setExecutionDatePlan(taskDto.getExecutionDatePlan());
        task.setExecutionDateFact(taskDto.getExecutionDateFact());
        task.setStarted(taskDto.getStarted());

        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    private List<Task> getAllTasks(User user) {
        if (userSessionService.isCurrentUserAdmin()) {
            return this.getAllTasks();
        } else {
            return taskRepository.findAllForUser(user.getId());
        }
    }

    @Override
    public List<TaskDto> getAllTaskDto() {
        User currentUser = userSessionService.getCurrentUser();
        if (userSessionService.isCurrentUserAdmin()) {
            return taskRepository.findAll()
                    .stream()
                    .map(this::createDto)
                    .collect(Collectors.toList());
        }

        return taskRepository.findAllForUser(currentUser.getId())
                .stream()
                .map(this::createDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Task> getTaskPageForTelBot(Long telUserId, Pageable pageable) throws UserNotFoundException {
        User user = userService.getUserByTelUserId(telUserId);
        List<UUID> ids = getActiveForExecutor(user).stream().map(Task::getId).collect(Collectors.toList());
        return taskRepository.findActiveTasksPage(ids, pageable);
    }

    @Override
    public TaskDto createDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());

        if (!ObjectUtils.isEmpty(task.getProject())) {
            taskDto.setProject(projectService.createProjectDto(task.getProject()));
        }

        if (!ObjectUtils.isEmpty(task.getCreator())) {
            taskDto.setCreator(userService.createUserDtoFromUser(task.getCreator()));
        }

        if (!ObjectUtils.isEmpty(task.getExecutor())) {
            taskDto.setExecutor(userService.createUserDtoFromUser(task.getExecutor()));
        }

        if (!ObjectUtils.isEmpty(task.getInitiator())) {
            taskDto.setInitiator(userService.createUserDtoFromUser(task.getInitiator()));
        }

        taskDto.setPriority(task.getPriority());
        taskDto.setNumber(task.getNumber());
        taskDto.setOverdue(task.getOverdue());
        taskDto.setDescription(task.getDescription());
        taskDto.setComment(task.getComment());
        taskDto.setState(task.getState());
        taskDto.setKanbanState(task.getKanbanState());
        taskDto.setKanbanOrder(task.getKanbanOrder());
        taskDto.setExecutionDatePlan(task.getExecutionDatePlan());
        taskDto.setExecutionDateFact(task.getExecutionDateFact());
        taskDto.setStarted(task.getStarted());

        long days = getDaysUntilDueDate(taskDto.getExecutionDatePlan());
        taskDto.setDaysUntilDueDate(days);

        return taskDto;
    }

    @Override
    public TelegramTaskDto createTelegramDto(Task task) {
        TelegramTaskDto taskDto = new TelegramTaskDto();
        taskDto.setId(task.getId());
        taskDto.setNumber(task.getNumber());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getExecutionDatePlan());
        taskDto.setPriority(task.getPriority());
        taskDto.setComment(task.getComment());
        taskDto.setProject(task.getProject() != null ? task.getProject().getName() : null);
        return taskDto;
    }

    @Override
    public String getNextTaskNumber() {
        List<String> numbers = taskRepository.findAll().stream().map(Task::getNumber).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(numbers)) {
            List<Integer> result = new ArrayList<>();

            numbers.forEach(nextNumber -> {
                if (nextNumber.startsWith(TASK_PREFIX)) {
                    nextNumber = nextNumber.replace(TASK_PREFIX, "");
                    if (Character.isDigit(nextNumber.charAt(0))) {
                        StringBuilder num = new StringBuilder();

                        for (int i = 0; i < nextNumber.length(); i++) {
                            if (Character.isDigit(nextNumber.charAt(i))) {
                                num.append(nextNumber.charAt(i));
                            } else {
                                break;
                            }
                        }
                        Integer number = parseNumber(num.toString());
                        if (number != null)
                            result.add(number);
                    }
                }
            });

            if (!CollectionUtils.isEmpty(result)) {
                Collections.sort(result);
                return TASK_PREFIX + (result.get(result.size() - 1) + 1);
            }
        }

        return TASK_PREFIX + 1;
    }

    @Override
    public void changeKanbanState(KanbanRequest[] kanbanRequest) throws TaskNotFoundException {
        for (KanbanRequest request : kanbanRequest) {
            Task task = getTaskById(UUID.fromString(request.getTaskId()));
            if (task != null) {
                task.setKanbanState(request.getState());
                task.setKanbanOrder(request.getOrder());
                taskRepository.save(task);
            }
        }

    }

    @Override
    public void checkOverdueTasks() {
        List<Task> tasks = taskRepository.findActiveWithDueDate();
        tasks.forEach(this::checkOverdue);
    }

    private List<Task> getActiveForExecutor(User user) {
        return taskRepository.findActiveForExecutor(user.getId(),
                Arrays.asList(Task.STATE_ASSIGNED, Task.STATE_REWORK));
    }

    private long getDaysUntilDueDate(Date executionDatePlan) {
        if (ObjectUtils.isEmpty(executionDatePlan))
            return 0L;
        LocalDateTime in = LocalDateTime.ofInstant(executionDatePlan.toInstant(), ZoneId.systemDefault());
        return Duration.between(LocalDateTime.now(), in).toDays();

    }

    private void checkOverdue(Task task) {
        Date currentData = new Date();
        if (task.getExecutionDatePlan().before(currentData)) {
            if (BooleanUtils.isNotTrue(task.getOverdue())) {
                task.setOverdue(true);
                String title = getMessage(sourcePath, "notification.task.title", task.getNumber());
                String message = getMessage(sourcePath, "notification.task.overdue", task.getNumber());
                User executor = task.getExecutor();
                notificationService.createAndSendNotification(executor, title, message, Notification.DANGER, Notification.WORKFLOW, task);
                if (BooleanUtils.isTrue(executor.getTelLogged()) && executor.getTelUserId() != null && executor.getTelChatId() != null) {
                    telegramService.sendOverdueTaskMessage(executor, task);
                }
                taskRepository.save(task);
            }
        }
    }

    private Integer parseNumber(String nextNumber) {
        int number;
        nextNumber = nextNumber.replaceAll("[^0-9]", "");
        try {
            number = Integer.parseInt(nextNumber);
        } catch (NumberFormatException e) {
            return null;
        }

        return number;
    }


}
