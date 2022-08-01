package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.application.configuration.constant.ApplicationConstants;
import com.pentyugov.wflow.application.utils.ApplicationUtils;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.TelegramTaskDto;
import com.pentyugov.wflow.core.dto.TelegramUserDto;
import com.pentyugov.wflow.core.service.EmailService;
import com.pentyugov.wflow.core.service.TaskService;
import com.pentyugov.wflow.core.service.TelegramService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.request.TelegramLoginUserRequest;
import com.pentyugov.wflow.web.payload.request.TelegramTaskSendMessageRequest;
import com.pentyugov.wflow.web.payload.request.TelegramVerifyCodeRequest;
import com.pentyugov.wflow.web.payload.response.TelegramLoginUserResponse;
import com.pentyugov.wflow.web.payload.response.TelegramTaskSendMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(TelegramService.NAME)
public class TelegramServiceImpl implements TelegramService {

    private final Logger logger = LoggerFactory.getLogger(TelegramServiceImpl.class);

    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ApplicationUtils applicationUtils;

    private final Map<Long, String> codesMap = new HashMap<>();

    public TelegramServiceImpl(EmailService emailService, BCryptPasswordEncoder passwordEncoder, UserService userService, ApplicationUtils applicationUtils) {
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.applicationUtils = applicationUtils;
    }

    @Override
    public List<TelegramUserDto> getLoggedUsers() {
        return userService
                .findAllLoggedInTelegram()
                .stream()
                .map(this::createTelegramUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public TelegramLoginUserResponse loginTelegramUser(TelegramLoginUserRequest request) throws UserNotFoundException {
        User user = userService.getUserByUsername(request.getUsername());
        user.setTelUserId(request.getTelUserId());
        user.setTelChatId(request.getTelChatId());
        userService.updateUser(user);
        String rawCode = applicationUtils.getVerificationCode();
        String hashCode = applicationUtils.encodeString(rawCode);
        codesMap.put(request.getTelUserId(), hashCode);
        emailService.sentTelegramVerificationCodeMail(user, rawCode);
        TelegramLoginUserResponse telegramLoginUserResponse = new TelegramLoginUserResponse();
        telegramLoginUserResponse.setUserId(user.getId().toString());
        telegramLoginUserResponse.setHashCode(hashCode);
        return telegramLoginUserResponse;
    }

    @Override
    public boolean verifyCode(TelegramVerifyCodeRequest request) throws UserNotFoundException {
        String code = request.getCode();
        String hashCode = codesMap.get(request.getTelUserId());
        if (StringUtils.hasText(hashCode) && StringUtils.hasText(code)) {
            User user = userService.getUserByTelUserId(request.getTelUserId());
            boolean verified = passwordEncoder.matches(code, hashCode);
            if (verified) {
                codesMap.remove(request.getTelUserId());
                user.setTelLogged(true);
                userService.updateUser(user);
            }
            return verified;
        }
        return false;
    }

    @Override
    public void sendAssignedTaskMessage(User user, Task task) {
        sendTaskMessage(user, task, TelegramTaskSendMessageRequest.TYPE_ASSIGNED);
    }


    @Override
    public void sendOverdueTaskMessage(User user, Task task) {
        sendTaskMessage(user, task, TelegramTaskSendMessageRequest.TYPE_OVERDUE);
    }

    private void sendTaskMessage(User user, Task task, Integer type) {
        TelegramTaskSendMessageRequest request = new TelegramTaskSendMessageRequest();
        request.setType(type);
        request.setTelUserId(user.getTelUserId());
        request.setTelChatId(user.getTelChatId());
        request.setTask(createTelegramTaskDto(task));

        RestTemplate template = new RestTemplate();

        try {
            template.postForObject(ApplicationConstants.TelBot.TASKS_SEND_MESSAGE_ENDPOINT, request, TelegramTaskSendMessageResponse.class);
        } catch (HttpClientErrorException e) {
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
        return telegramUserDto;
    }
}
