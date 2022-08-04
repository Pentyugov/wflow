package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Notification;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.NotificationDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface NotificationService {

    String NAME = "wflow$NotificationService";

    Page<Notification> getNotificationPage(Optional<Integer> page, Optional<String> sortBy);

    List<Notification> getNotificationsForCurrentUser();

    List<Notification> getNotificationsByReceiverId(UUID receiverId);

    Notification createNewNotification(NotificationDto notificationDto) throws UserNotFoundException;

    Notification createNotificationFromDto(NotificationDto notificationDto) throws UserNotFoundException;

    NotificationDto createNotificationDtoFromNotification(Notification notification);

    void sendNotificationWithWs(NotificationDto notificationDto, UUID recientId);

    void deleteNotification(UUID id);

    void saveNotification(Notification notification);

    Notification createNotification(String title, String message, int type, int accessoryType, User receiver, Card card);

    void createAndSendNotification(User user, String title, String message, int type, int accessoryType, Card card);

}
