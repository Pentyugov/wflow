package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.core.service.TaskService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import com.pentyugov.wflow.web.payload.request.KanbanRequest;
import com.pentyugov.wflow.web.payload.request.TaskSignalProcRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.pentyugov.wflow.application.configuration.SwaggerConfig.BEARER;

@RestController
@RequestMapping("/api/tasks")
public class TaskController extends ExceptionHandling {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(taskService.getAllTaskDto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getById(@PathVariable String id) throws TaskNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        return new ResponseEntity<>(taskService.createDto(task), HttpStatus.OK);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active tasks for current user", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getActive() {
        List<TaskDto> result = taskService.getActiveForExecutor()
                .stream()
                .map(taskService::createDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/productivity-data")
    @Operation(summary = "Get current user`s productivity data", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getProductivityData() {
        List<TaskDto> result = taskService.getProductivityData()
                .stream()
                .map(taskService::createDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Get task history", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> getTaskHistory(@PathVariable String id) throws TaskNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        return new ResponseEntity<>(taskService.getTaskHistory(task), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Post new task", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> post(@RequestBody TaskDto taskDto) throws ProjectNotFoundException, UserNotFoundException {
        Task task = taskService.createNewTask(taskDto);
        return new ResponseEntity<>(taskService.createDto(task), HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update existing task", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> update(@RequestBody TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException {
        Task task = taskService.updateTask(taskDto);
        return new ResponseEntity<>(taskService.createDto(task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Get task by ID", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) throws TaskNotFoundException {
        taskService.deleteTask(UUID.fromString(id));
        String message = String.format("Task with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/signal-proc")
    @Operation(summary = "Signal task proc to switch state", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<HttpResponse> signalProcAction(@RequestBody TaskSignalProcRequest taskSignalProcRequest) throws TaskNotFoundException {
        String message = taskService.signalProc(taskSignalProcRequest);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/kanban")
    @Operation(summary = "Get task`s kanban state", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<HttpResponse> changeKanbanState(@RequestBody KanbanRequest[] kanbanRequest) throws TaskNotFoundException {
        taskService.changeKanbanState(kanbanRequest);
        return response(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
    }

    @PostMapping("/filter")
    @Operation(summary = "Search task with filter", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> applyTaskFilters(@RequestBody FiltersRequest filtersRequest) throws UserNotFoundException {
        List<TaskDto> result = taskService
                .getTasksWithFilters(filtersRequest)
                .stream()
                .map(taskService::createDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
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
