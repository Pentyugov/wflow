package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Notification;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.NotificationDto;
import com.pentyugov.wflow.core.repository.NotificationRepository;
import com.pentyugov.wflow.core.service.NotificationService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Websocket.NOTIFICATION_DESTINATION;

@Service(NotificationService.NAME)
@RequiredArgsConstructor
public class NotificationServiceImpl extends AbstractService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public Page<Notification> getNotificationPage(Optional<Integer> page, Optional<String> sortBy) {
        return notificationRepository.findAll(
                PageRequest.of(
                        page.orElse(0),
                        5,
                        Sort.Direction.DESC, sortBy.orElse("createDate")));

    }

    @Override
    public List<Notification> getNotificationsForCurrentUser(Principal principal) throws UserNotFoundException {
        User currentUser = userService.getUserByPrincipal(principal);
        return getNotificationsByReceiverId(currentUser.getId());
    }

    @Override
    public List<Notification> getNotificationsByReceiverId(UUID receiverId) {
        return notificationRepository.findByReceiverId(receiverId);
    }

    @Override
    public Notification createNewNotification(NotificationDto notificationDto) throws UserNotFoundException {
        Notification notification = createNotificationFromDto(notificationDto);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification createNotificationFromDto(NotificationDto notificationDto) throws UserNotFoundException {
        Notification notification = new Notification();
        notification.setTitle(notificationDto.getTitle());
        notification.setMessage(notificationDto.getMessage());
        notification.setType(notificationDto.getType());
        notification.setRead(notificationDto.getRead());
        notification.setReceiver(userService.getUserById(notificationDto.getReceiverId()));
        return notification;
    }

    @Override
    public NotificationDto createNotificationDtoFromNotification(Notification notification) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setTitle(notification.getTitle());
        notificationDto.setMessage(notification.getMessage());
        notificationDto.setType(notification.getType());
        notificationDto.setAccessoryType(notification.getAccessoryType());
        notificationDto.setRead(notification.getRead());
        notificationDto.setReceiverId(notification.getReceiver().getId());
        notificationDto.setCardId(notification.getCard() != null ? notification.getCard().getId() : null);
        notificationDto.setCreateDate(Date.from(notification.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()));
        return notificationDto;
    }

    @Override
    public void sendNotificationWithWs(NotificationDto notificationDto, UUID recipientId) {
        messagingTemplate.convertAndSendToUser(recipientId.toString(), NOTIFICATION_DESTINATION, notificationDto);
    }

    @Override
    public void deleteNotification(UUID id) {
        this.notificationRepository.delete(id);
    }

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public Notification createNotification(String title, String message, int type, int accessoryType, User receiver, Card card) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setAccessoryType(accessoryType);
        notification.setReceiver(receiver);
        notification.setCard(card);
        notification.setRead(false);
        return notification;
    }

    @Override
    public void createAndSendNotification(User user, String title, String message, int type, int accessoryType, Card card) {
        Notification notification = createNotification(title, message, type, accessoryType, user, card);
        saveNotification(notification);
        sendNotificationWithWs(createNotificationDtoFromNotification(notification), notification.getReceiver().getId());
    }

}
