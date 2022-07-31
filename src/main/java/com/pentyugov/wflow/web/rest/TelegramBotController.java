package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.service.TelegramService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.TelegramLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.TelegramVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.TelegramLoggedUsersResponse;
import com.pentyugov.wflow.web.payload.response.TelegramLoginUserResponse;
import com.pentyugov.wflow.web.payload.response.TelegramVerifyCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/telegram")
public class TelegramBotController {

    private final TelegramService telegramService;

    @Autowired
    public TelegramBotController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

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
}
