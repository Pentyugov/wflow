package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.ChatRoom;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ChatRoomService {

    String NAME = "wflow$ChatRoomService";

    Optional<String> getChatId(UUID senderId, UUID recipientId, boolean createIfNotExist) throws UserNotFoundException;

    List<ChatRoom> getRoomsForUser(User user);
}
