package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import com.pentyugov.wflow.core.service.TaskService;
import com.pentyugov.wflow.core.service.TelegramService;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.telbot.TelegramGetTaskPageRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelegramLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelegramVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.telbot.TelegramGetTaskPageResponse;
import com.pentyugov.wflow.web.payload.response.telbot.TelegramLoggedUsersResponse;
import com.pentyugov.wflow.web.payload.response.telbot.TelegramLoginUserResponse;
import com.pentyugov.wflow.web.payload.response.telbot.TelegramVerifyCodeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

import static com.pentyugov.wflow.application.configuration.SwaggerConfig.BEARER;

@RestController
@RequestMapping("/api/v1/telbot")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TelegramBotController {

    private final TelegramService telegramService;
    private final TaskService taskService;

    @PostMapping("/check-connection")
    @Operation(summary = "Telbot check connection with server", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<Object> checkConnection() {
        return ResponseEntity
                .ok()
                .body(null);
    }

    @GetMapping("/logged-users")
    @Operation(summary = "Telbot gets info about logged users", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<TelegramLoggedUsersResponse> getLoggedUsers() {
        TelegramLoggedUsersResponse response = new TelegramLoggedUsersResponse();
        response.setUsers(telegramService.getLoggedUsers());
        response.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Login telbot user", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<TelegramLoginUserResponse> loginTelegramUser(@RequestBody TelegramLoginUserRequest request) throws UserNotFoundException {
        return new ResponseEntity<>(telegramService.loginTelegramUser(request), HttpStatus.OK);
    }

    @PostMapping("/verify-code")
    @Operation(summary = "Check telbot verification code", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<TelegramVerifyCodeResponse> verifyCode(@RequestBody TelegramVerifyCodeRequest request) throws UserNotFoundException {
        boolean verified = telegramService.verifyCode(request);
        TelegramVerifyCodeResponse response = new TelegramVerifyCodeResponse();
        response.setVerified(verified);
        response.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/tasks/page")
    @Operation(summary = "Get task page", security = @SecurityRequirement(name = BEARER))
    public TelegramGetTaskPageResponse getTaskPage(@RequestBody TelegramGetTaskPageRequest request) throws UserNotFoundException {
        TelegramGetTaskPageResponse response = new TelegramGetTaskPageResponse();
        Page<Task> resultPage = taskService.getTaskPageForTelBot(request.getTelUserId(), PageRequest.of(
                request.getPage(),
                10,
                Sort.Direction.DESC, request.getSortBy()));

        response.setPage(resultPage.getPageable().getPageNumber());
        response.setTasks(resultPage.getContent().stream().map(taskService::createTelegramDto).collect(Collectors.toList()));
        return response;
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "Get task by id", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<TelegramTaskDto> getTask(@PathVariable String id) throws TaskNotFoundException {
        Task task = taskService.getTaskById(UUID.fromString(id));
        return new ResponseEntity<>(taskService.createTelegramDto(task), HttpStatus.OK);
    }


}
