package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.ChatRoom;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.repository.ChatRoomRepository;
import com.pentyugov.wflow.core.service.ChatRoomService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service(ChatRoomService.NAME)
@RequiredArgsConstructor
public class ChatRoomServiceImpl extends AbstractService implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public Optional<String> getChatId(UUID senderId, UUID recipientId, boolean createIfNotExist) throws UserNotFoundException {
        User sender = userService.getUserById(senderId);
        User recipient = userService.getUserById(recipientId);
        return chatRoomRepository
                .findBySenderAndRecipient(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createIfNotExist) {
                        return  Optional.empty();
                    }
                    String chatId = String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = new ChatRoom();
                    senderRecipient.setChatId(chatId);
                    senderRecipient.setSender(sender);
                    senderRecipient.setRecipient(recipient);

                    ChatRoom recipientSender = new ChatRoom();
                    recipientSender.setChatId(chatId);
                    recipientSender.setSender(recipient);
                    recipientSender.setRecipient(sender);

                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }

    public List<ChatRoom> getRoomsForUser(User user) {
        return chatRoomRepository.findRoomsForUser(user.getId());
    }
}
