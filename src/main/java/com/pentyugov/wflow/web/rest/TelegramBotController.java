package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.service.TaskService;
import com.pentyugov.wflow.core.service.TelegramService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.TelegramGetTaskPageRequest;
import com.pentyugov.wflow.web.payload.request.TelegramLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.TelegramVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.TelegramGetTaskPageResponse;
import com.pentyugov.wflow.web.payload.response.TelegramLoggedUsersResponse;
import com.pentyugov.wflow.web.payload.response.TelegramLoginUserResponse;
import com.pentyugov.wflow.web.payload.response.TelegramVerifyCodeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static com.pentyugov.wflow.application.configuration.SwaggerConfig.BEARER;

@RestController
@RequestMapping("/api/v1/telegram")
@RequiredArgsConstructor
public class TelegramBotController {

    private final TelegramService telegramService;
    private final TaskService taskService;

    @GetMapping("/logged-users")
    public ResponseEntity<TelegramLoggedUsersResponse> getLoggedUsers() {
        TelegramLoggedUsersResponse response = new TelegramLoggedUsersResponse();
        response.setUsers(telegramService.getLoggedUsers());
        response.setHttpStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TelegramLoginUserResponse> loginTelegramUser(@RequestBody TelegramLoginUserRequest request) throws UserNotFoundException {
        return new ResponseEntity<>(telegramService.loginTelegramUser(request), HttpStatus.OK);
    }

    @PostMapping("/verify-code")
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
}
