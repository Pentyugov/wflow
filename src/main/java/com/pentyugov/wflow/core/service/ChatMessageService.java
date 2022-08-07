package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.ChatMessage;
import com.pentyugov.wflow.core.dto.ChatMessageDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ChatMessageService {

    String NAME = "wflow$ChatMessageService";

    ChatMessage save(ChatMessage chatMessage);

    ChatMessage update(ChatMessageDto chatMessageDto) throws UserNotFoundException;

    ChatMessage convert(ChatMessageDto chatMessageDto) throws UserNotFoundException;

    ChatMessageDto convert(ChatMessage chatMessage);

    List<ChatMessage> getChatMessagesByChatId(String chatId);

    Page<ChatMessage> getPageChatMessagesByChatId(String chatId, Optional<Integer> page, Optional<String> sortBy);

    Integer getNewChatMessagesCountForUser(UUID userId);

    List<ChatMessageDto> getChatMessagesForUserWithStatus(UUID userId, int status);

    List<ChatMessageDto> getNewMessagesForUser(UUID userId);

    ChatMessage getById(UUID uuid);
}
