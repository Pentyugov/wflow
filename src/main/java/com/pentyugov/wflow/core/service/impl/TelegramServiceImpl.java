package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.application.configuration.constant.ApplicationConstants;
import com.pentyugov.wflow.application.utils.ApplicationUtils;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.domain.entity.UserSettings;
import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import com.pentyugov.wflow.core.dto.TelegramUserDto;
import com.pentyugov.wflow.core.service.EmailService;
import com.pentyugov.wflow.core.service.TelegramService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.core.service.UserSettingsService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotTaskSendMessageRequest;
import com.pentyugov.wflow.web.payload.request.telbot.TelbotVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.telbot.TelbotLoginUserResponse;
import com.pentyugov.wflow.web.payload.response.telbot.TelbotTaskSendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(TelegramService.NAME)
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final Logger logger = LoggerFactory.getLogger(TelegramServiceImpl.class);

    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ApplicationUtils applicationUtils;
    private final UserSettingsService userSettingsService;
    private final Map<Long, String> codesMap = new HashMap<>();

    @Override
    public List<TelegramUserDto> getLoggedUsers() {
        return userService
                .findAllLoggedInTelegram()
                .stream()
                .map(this::createTelegramUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public TelbotLoginUserResponse loginTelegramUser(TelbotLoginUserRequest request) throws UserNotFoundException {
        User user = userService.getByUsername(request.getUsername());
        user.setTelUserId(request.getTelUserId());
        user.setTelChatId(request.getTelChatId());
        userService.update(user);
        UserSettings userSettings = userSettingsService.getUserSettings(user);
        userSettings.setTelbotCalendarNotification(true);
        userSettings.setTelbotTaskNotification(true);
        userSettingsService.updateUserSettings(userSettings);
        String rawCode = applicationUtils.getVerificationCode();
        String hashCode = applicationUtils.encodeString(rawCode);
        codesMap.put(request.getTelUserId(), hashCode);
        emailService.sentTelegramVerificationCodeMail(user, rawCode);
        TelbotLoginUserResponse telbotLoginUserResponse = new TelbotLoginUserResponse();
        telbotLoginUserResponse.setUserId(user.getId().toString());
        telbotLoginUserResponse.setHashCode(hashCode);
        return telbotLoginUserResponse;
    }

    @Override
    public boolean verifyCode(TelbotVerifyCodeRequest request) throws UserNotFoundException {
        String code = request.getCode();
        String hashCode = codesMap.get(request.getTelUserId());
        if (StringUtils.hasText(hashCode) && StringUtils.hasText(code)) {
            User user = userService.getByTelUserId(request.getTelUserId());
            boolean verified = passwordEncoder.matches(code, hashCode);
            if (verified) {
                codesMap.remove(request.getTelUserId());
                user.setTelLogged(true);
                userService.update(user);
            }
            return verified;
        }
        return false;
    }

    @Override
    public void sendAssignedTaskMessage(User user, Task task) {
        sendTaskMessage(user, task, TelbotTaskSendMessageRequest.TYPE_ASSIGNED);
    }


    @Override
    public void sendOverdueTaskMessage(User user, Task task) {
        sendTaskMessage(user, task, TelbotTaskSendMessageRequest.TYPE_OVERDUE);
    }

    @Override
    public void updateTelUserSettings(Long telUserId, TelegramUserDto.TelUserSettings userSettings) throws UserNotFoundException {
        User user = userService.getByTelUserId(telUserId);
        UserSettings settings = userSettingsService.getUserSettings(user);
        settings.setTelbotTaskNotification(userSettings.getSubscribeOnTasks());
        settings.setTelbotCalendarNotification(userSettings.getSubscribeOnCalendar());
        userSettingsService.updateUserSettings(settings);
    }

    private void sendTaskMessage(User user, Task task, Integer type) {
        TelbotTaskSendMessageRequest request = new TelbotTaskSendMessageRequest();
        request.setType(type);
        request.setTelUserId(user.getTelUserId());
        request.setTelChatId(user.getTelChatId());
        request.setTask(createTelegramTaskDto(task));

        RestTemplate template = new RestTemplate();

        try {
            template.postForObject(ApplicationConstants.TelBot.TASKS_SEND_MESSAGE_ENDPOINT, request, TelbotTaskSendMessageResponse.class);
        } catch (HttpClientErrorException | ResourceAccessException e) {
            logger.error(e.getMessage());
        }
    }

    private TelegramTaskDto createTelegramTaskDto(Task task) {
        TelegramTaskDto taskDto = new TelegramTaskDto();
        taskDto.setNumber(task.getNumber());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getExecutionDatePlan());
        taskDto.setPriority(task.getPriority());
        taskDto.setComment(task.getComment());
        taskDto.setProject(task.getProject() != null ? task.getProject().getName() : null);
        return taskDto;
    }

    private TelegramUserDto createTelegramUserDto(User user) {
        TelegramUserDto telegramUserDto = new TelegramUserDto();
        telegramUserDto.setUserId(user.getId());
        telegramUserDto.setTelUserId(user.getTelUserId());
        telegramUserDto.setTelChatId(user.getTelChatId());
        UserSettings userSettings = userSettingsService.getUserSettings(user);
        telegramUserDto.setUserSettings(new TelegramUserDto.TelUserSettings(userSettings));
        return telegramUserDto;
    }
}
