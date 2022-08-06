package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import com.pentyugov.wflow.core.service.TaskService;
import com.pentyugov.wflow.core.service.TelegramService;
import com.pentyugov.wflow.web.exception.TaskNotFoundException;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotGetTaskPageRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotUpdateUserSettingsRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.telbot.TelbotGetTaskPageResponse;
import com.pentyugov.wflow.web.payload.response.telbot.TelbotLoggedUsersResponse;
import com.pentyugov.wflow.web.payload.response.telbot.TelbotLoginUserResponse;
import com.pentyugov.wflow.web.payload.response.telbot.TelbotVerifyCodeResponse;
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
    public ResponseEntity<TelbotLoggedUsersResponse> getLoggedUsers() {
        TelbotLoggedUsersResponse response = new TelbotLoggedUsersResponse();
        response.setUsers(telegramService.getLoggedUsers());
        response.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Login telbot user", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<TelbotLoginUserResponse> loginTelegramUser(@RequestBody TelbotLoginUserRequest request) throws UserNotFoundException {
        return new ResponseEntity<>(telegramService.loginTelegramUser(request), HttpStatus.OK);
    }

    @PostMapping("/verify-code")
    @Operation(summary = "Check telbot verification code", security = @SecurityRequirement(name = BEARER))
    public ResponseEntity<TelbotVerifyCodeResponse> verifyCode(@RequestBody TelbotVerifyCodeRequest request) throws UserNotFoundException {
        boolean verified = telegramService.verifyCode(request);
        TelbotVerifyCodeResponse response = new TelbotVerifyCodeResponse();
        response.setVerified(verified);
        response.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/tasks/page")
    @Operation(summary = "Get task page", security = @SecurityRequirement(name = BEARER))
    public TelbotGetTaskPageResponse getTaskPage(@RequestBody TelbotGetTaskPageRequest request) throws UserNotFoundException {
        TelbotGetTaskPageResponse response = new TelbotGetTaskPageResponse();
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

    @PutMapping("/settings")
    public ResponseEntity<Object> updateUserSettings(@RequestBody TelbotUpdateUserSettingsRequest request) throws UserNotFoundException {
        telegramService.updateTelUserSettings(request.getTelUserId(), request.getUserSettings());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }


}
