package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Notification;
import com.pentyugov.wflow.core.dto.NotificationDto;
import com.pentyugov.wflow.core.service.NotificationService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController extends ExceptionHandling {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<NotificationDto> notificationList = notificationService.getNotificationsForCurrentUser()
                .stream()
                .map(notificationService::convert)
                .collect(Collectors.toList());

        return new ResponseEntity<>(notificationList, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody NotificationDto notificationDto) throws UserNotFoundException {
        Notification notification = notificationService.add(notificationDto);
        return new ResponseEntity<>(notificationService.convert(notification), HttpStatus.OK);

    }

    @GetMapping("/page")
    public List<NotificationDto> getPage(@RequestParam Optional<Integer> page,
                                         @RequestParam Optional<String> sortBy) {
        List<NotificationDto> result = new ArrayList<>();
        notificationService.getNotificationPage(page, sortBy).getContent().forEach(notification -> {
            result.add(notificationService.convert(notification));
        });

        return result;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        notificationService.deleteNotification(UUID.fromString(id));
        String message = String.format("Notification with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
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
