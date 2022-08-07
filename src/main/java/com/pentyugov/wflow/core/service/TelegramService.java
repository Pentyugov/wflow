package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.TelegramUserDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.telbot.TelbotLoginUserResponse;

import java.util.List;

public interface TelegramService {
    String NAME = "wflow$TelegramService";

    List<TelegramUserDto> getLoggedUsers();

    TelbotLoginUserResponse loginTelegramUser(TelbotLoginUserRequest request) throws UserNotFoundException;

    void logoutTelegramUser(Long telUserId) throws UserNotFoundException;

    boolean verifyCode(TelbotVerifyCodeRequest request) throws UserNotFoundException;

    void sendAssignedTaskMessage(User user, Task task);

    void sendOverdueTaskMessage(User user, Task task);

    void updateTelUserSettings(Long telUserId, TelegramUserDto.TelUserSettings userSettings) throws UserNotFoundException;
}
