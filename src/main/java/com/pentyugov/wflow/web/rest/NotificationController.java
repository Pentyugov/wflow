package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Notification;
import com.pentyugov.wflow.core.dto.NotificationDto;
import com.pentyugov.wflow.core.service.NotificationService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notification")
public class NotificationController extends ExceptionHandling {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping("/add-new-notification")
    public ResponseEntity<Object> createNotification(@RequestBody NotificationDto notificationDto) throws UserNotFoundException {
        Notification notification = notificationService.createNewNotification(notificationDto);
        return new ResponseEntity<>(notificationService.createNotificationDtoFromNotification(notification), HttpStatus.OK);

    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllNotificationsFor(Principal principal) throws UserNotFoundException {
        List<NotificationDto> notificationList = notificationService.getNotificationsForCurrentUser(principal)
                .stream()
                .map(notificationService::createNotificationDtoFromNotification)
                .collect(Collectors.toList());

        return new ResponseEntity<>(notificationList, HttpStatus.OK);

    }

    @GetMapping("/get-notification-page")
    public List<NotificationDto> getNotificationPage(@RequestParam Optional<Integer> page,
                                                     @RequestParam Optional<String> sortBy) {
        List<NotificationDto> result = new ArrayList<>();
        notificationService.getNotificationPage(page, sortBy).getContent().forEach(notification -> {
            result.add(notificationService.createNotificationDtoFromNotification(notification));
        });

        return result;
    }

    @GetMapping("/get-notification-page-for-receiver")
    public List<NotificationDto> getNotificationPageForReceiver(@RequestParam Optional<Integer> page,
                                                                @RequestParam Optional<String> sortBy,
                                                                Principal principal) throws UserNotFoundException {
        List<NotificationDto> result = new ArrayList<>();
        notificationService.getNotificationPageForCurrentUser(page, sortBy, principal).getContent().forEach(notification ->
                result.add(notificationService.createNotificationDtoFromNotification(notification)));

        return result;
    }

    @DeleteMapping("/delete-notification/{id}")
    public ResponseEntity<HttpResponse> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(UUID.fromString(id));
        String message = String.format("Notification with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }

    @PostMapping("/send-test-notification")
    public void sendTestNotification(Principal principal) throws UserNotFoundException {
        Notification notification = new Notification();
        notification.setType(10);
        notification.setAccessoryType(20);
        notification.setRead(false);
        notification.setTitle("WS SERVICE TEST");
        notification.setMessage("test notification ws service");
        notification.setReceiver(userService.getUserByPrincipal(principal));
        notificationService.saveNotification(notification);
        notificationService.sendNotificationWithWs(notificationService.createNotificationDtoFromNotification(notification), notification.getReceiver().getId());
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
