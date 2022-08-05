package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.TelegramUserDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.telbot.TelegramLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelegramVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.telbot.TelegramLoginUserResponse;

import java.util.List;

public interface TelegramService {
    String NAME = "wflow$TelegramService";

    List<TelegramUserDto> getLoggedUsers();

    TelegramLoginUserResponse loginTelegramUser(TelegramLoginUserRequest request) throws UserNotFoundException;

    boolean verifyCode(TelegramVerifyCodeRequest request) throws UserNotFoundException;

    void sendAssignedTaskMessage(User user, Task task);

    void sendOverdueTaskMessage(User user, Task task);
}
