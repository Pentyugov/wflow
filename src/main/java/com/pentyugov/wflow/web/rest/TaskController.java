package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.TaskDto;
import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import com.pentyugov.wflow.core.service.TaskService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.exception.ProjectNotFoundException;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import com.pentyugov.wflow.web.payload.request.KanbanRequest;
import com.pentyugov.wflow.web.payload.request.FiltersRequest;
import com.pentyugov.wflow.web.payload.request.TaskSignalProcRequest;
import com.pentyugov.wflow.web.payload.request.TelegramGetTaskPageRequest;
import com.pentyugov.wflow.web.payload.response.TelegramGetTaskPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController extends ExceptionHandling {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll(Principal principal) throws UserNotFoundException {
        return new ResponseEntity<>(taskService.getAllTaskDto(principal), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) throws TaskNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        return new ResponseEntity<>(taskService.createDto(task), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<Object> getActive(Principal principal) throws UserNotFoundException {
        List<TaskDto> result = taskService.getActiveForExecutor(principal)
                .stream()
                .map(taskService::createDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/productivity-data")
    public ResponseEntity<Object> getProductivityData(Principal principal) throws UserNotFoundException {
        List<TaskDto> result = taskService.getProductivityData(principal)
                .stream()
                .map(taskService::createDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<Object> getTaskHistory(@PathVariable String id) throws TaskNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        return new ResponseEntity<>(taskService.getTaskHistory(task), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TaskDto taskDto, Principal principal) throws UserNotFoundException, ProjectNotFoundException {
        Task task = taskService.createNewTask(taskDto, principal);
        return new ResponseEntity<>(taskService.createDto(task), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody TaskDto taskDto) throws UserNotFoundException, ProjectNotFoundException {
        Task task = taskService.updateTask(taskDto);
        return new ResponseEntity<>(taskService.createDto(task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id, Principal principal) throws TaskNotFoundException, UserNotFoundException {
        taskService.deleteTask(UUID.fromString(id), principal);
        String message = String.format("Task with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/signal-proc")
    public ResponseEntity<HttpResponse> signalProcAction(@RequestBody TaskSignalProcRequest taskSignalProcRequest, Principal principal) throws UserNotFoundException, TaskNotFoundException {
        String message = taskService.signalProc(taskSignalProcRequest, principal);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/kanban")
    public ResponseEntity<HttpResponse> changeKanbanState(@RequestBody KanbanRequest[] kanbanRequest) throws TaskNotFoundException {
        taskService.changeKanbanState(kanbanRequest);
        return response(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
    }

    @PostMapping("/filter")
    public ResponseEntity<Object> applyTaskFilters(@RequestBody FiltersRequest filtersRequest, Principal principal) throws UserNotFoundException {
        List<TaskDto> result = taskService
                .getTasksWithFilters(principal, filtersRequest)
                .stream()
                .map(taskService::createDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @GetMapping("/page")
//    public TelegramGetTaskPageResponse getTaskPage(@RequestParam Optional<Integer> page,
//                                  @RequestParam Optional<String> sortBy,
//                                  Principal principal) throws UserNotFoundException {
//        TelegramGetTaskPageResponse response = new TelegramGetTaskPageResponse();
//        Page<Task> resultPage = taskService.getPageForCurrentUser(page, sortBy, principal);
//        response.setPage(resultPage.getPageable().getPageNumber());
//        response.setTasks(resultPage.getContent().stream().map(taskService::createTelegramDto).collect(Collectors.toList()));
//        return response;
//
//    }

    @GetMapping("/page")
    public TelegramGetTaskPageResponse getTaskPage(TelegramGetTaskPageRequest request,
                                                   Principal principal) throws UserNotFoundException {
        TelegramGetTaskPageResponse response = new TelegramGetTaskPageResponse();
//        Page<Task> resultPage = taskService.getPageForCurrentUser(request.getPage(), request.getSortBy(), principal);
//        response.setPage(resultPage.getPageable().getPageNumber());
//        response.setTasks(resultPage.getContent().stream().map(taskService::createTelegramDto).collect(Collectors.toList()));
        return response;

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
