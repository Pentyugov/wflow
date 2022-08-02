package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.ChatMessage;
import com.pentyugov.wflow.core.dto.ChatMessageDto;
import com.pentyugov.wflow.core.service.ChatMessageService;
import com.pentyugov.wflow.core.service.ChatRoomService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Websocket.CHAT_MESSAGES_DESTINATION;
import static com.pentyugov.wflow.application.configuration.constant.ApplicationConstants.Websocket.NEW_MESSAGES_COUNT_DESTINATION;

@Controller
public class ChatController extends AbstractController {


    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) throws UserNotFoundException {
        ChatMessage chatMessage = chatMessageService.createChatMessageFromProxy(chatMessageDto);

        Optional<String> chatId = chatRoomService
                .getChatId(chatMessage.getSender().getId(), chatMessage.getRecipient().getId(), true);
        chatMessage.setChatId(chatId.orElse(null));
        ChatMessage saved = chatMessageService.save(chatMessage);
        ChatMessageDto proxy = chatMessageService.createProxyFromChatMessage(saved);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient().getId().toString(),
                CHAT_MESSAGES_DESTINATION,
                proxy);
        updateNewMessagesCount(proxy);
    }

    @MessageMapping("/chat/update-message")
    @Transactional
    public void updateMessage(@Payload ChatMessageDto chatMessageDto) throws UserNotFoundException {
        ChatMessage chatMessage = chatMessageService.updateMessage(chatMessageDto);
        chatMessageDto = chatMessageService.createProxyFromChatMessage(chatMessage);
        updateNewMessagesCount(chatMessageDto);
    }

    private void updateNewMessagesCount(ChatMessageDto chatMessageDto) {
        messagingTemplate.convertAndSendToUser(chatMessageDto.getRecipientId(), NEW_MESSAGES_COUNT_DESTINATION, chatMessageDto);
    }
}
