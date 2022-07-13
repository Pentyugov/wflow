package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.core.service.TaskService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import com.pentyugov.wflow.web.payload.request.TaskSignalProcRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController extends ExceptionHandling {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(Principal principal) throws UserNotFoundException {
        return new ResponseEntity<>(taskService.getAllTaskDto(principal), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        Task task = taskService.getTaskById(UUID.fromString(id));
        return new ResponseEntity<>(taskService.createProxyFromTask(task), HttpStatus.OK);
    }

    @GetMapping("/get-active-for-executor")
    public ResponseEntity<Object> getActive(Principal principal) throws UserNotFoundException {
        List<TaskDto> result = taskService.getActiveForExecutor(principal)
                .stream()
                .map(taskService::createProxyFromTask)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createNewTask(@RequestBody TaskDto taskDto, Principal principal) throws UserNotFoundException {
        Task task = taskService.createNewTask(taskDto, principal);
        return new ResponseEntity<>(taskService.createProxyFromTask(task), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateTask(@RequestBody TaskDto taskDto) throws UserNotFoundException {
        Task task = taskService.updateTask(taskDto);
        return new ResponseEntity<>(taskService.createProxyFromTask(task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<HttpResponse> deleteTask(@PathVariable String id) {
        taskService.deleteTask(UUID.fromString(id));
        String message = String.format("Task with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

    @GetMapping("/start-task/{id}")
    public ResponseEntity<HttpResponse> startTask(@PathVariable String id, Principal principal) throws UserNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        User currentUser = userService.getUserByPrincipal(principal);
        String message = taskService.startTask(task, currentUser);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/cancel-task/{id}")
    public ResponseEntity<HttpResponse> cancelTask(@PathVariable String id, @RequestBody String comment, Principal principal) throws UserNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        User currentUser = userService.getUserByPrincipal(principal);
        String message = taskService.cancelTask(task, currentUser, comment);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/execute-task/{id}")
    public ResponseEntity<HttpResponse> executeTask(@PathVariable String id, @RequestBody String comment, Principal principal) throws UserNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        User currentUser = userService.getUserByPrincipal(principal);
        String message = taskService.executeTask(task, currentUser, comment);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/rework-task/{id}")
    public ResponseEntity<HttpResponse> reworkTask(@PathVariable String id, @RequestBody String comment, Principal principal) throws UserNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        User currentUser = userService.getUserByPrincipal(principal);
        String message = taskService.reworkTask(task, currentUser, comment);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/finish-task/{id}")
    public ResponseEntity<HttpResponse> finishTask(@PathVariable String id, @RequestBody String comment, Principal principal) throws UserNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        User currentUser = userService.getUserByPrincipal(principal);
        String message = taskService.finishTask(task, currentUser, comment);
        return response(HttpStatus.OK, message);
    }

    @GetMapping("/get-history/{id}")
    public ResponseEntity<Object> getTaskHistory(@PathVariable String id) {
        Task task = taskService.getTaskById(UUID.fromString(id));
        return new ResponseEntity<>(taskService.getTaskHistory(task), HttpStatus.OK);
    }

    @GetMapping("/get-all-tasks")
    public ResponseEntity<Object> getAllTasks() {
        List<TaskDto> result = new ArrayList<>();
        taskService.getAllTasks().forEach(task -> result.add(taskService.createProxyFromTask(task)));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-tasks-current-user-executor-by-priority/{priority}")
    public ResponseEntity<Object> getTasksWhereCurrentUserExecutor(@PathVariable int priority, Principal principal) throws UserNotFoundException {
        List<TaskDto> result = new ArrayList<>();
        taskService.getTasksWhereCurrentUserExecutor(priority, principal).forEach(task ->
                result.add(taskService.createProxyFromTask(task)));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-tasks-with-priority/{priority}")
    public ResponseEntity<Object> getPriorityTaskForUser(@PathVariable int priority, Principal principal) throws UserNotFoundException {
        List<TaskDto> result = new ArrayList<>();
        taskService.getPriorityTasksForUser(priority, principal).forEach(task ->
                result.add(taskService.createProxyFromTask(task)));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-task-page-for-current-user")
    public List<TaskDto> getTaskPageForCurrentUser(@RequestParam Optional<Integer> page,
                                                   @RequestParam Optional<String> sortBy,
                                                   Principal principal) throws UserNotFoundException {
        List<TaskDto> result = new ArrayList<>();
        taskService.getPageForCurrentUser(page, sortBy, principal).getContent().forEach(task ->
                result.add(taskService.createProxyFromTask(task)));

        return result;
    }

    @PostMapping("/signal-proc")
    public ResponseEntity<HttpResponse> signalProcAction(@RequestBody TaskSignalProcRequest taskSignalProcRequest, Principal principal) throws UserNotFoundException, TaskNotFoundException {
        String message = taskService.signalProc(taskSignalProcRequest, principal);
        return response(HttpStatus.OK, message);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse();
        body.setTimeStamp(new Date());
        body.setHttpStatus(httpStatus);
        body.setHttpStatusCode(httpStatus.value());
        body.setReason(httpStatus.getReasonPhrase().toUpperCase());
        body.setMessage(message);
        return new ResponseEntity<>(body, httpStatus);
    }
}
