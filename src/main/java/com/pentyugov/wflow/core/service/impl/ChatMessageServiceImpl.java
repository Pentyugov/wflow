package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.ChatMessage;
import com.pentyugov.wflow.core.dto.ChatMessageDto;
import com.pentyugov.wflow.core.repository.ChatRepository;
import com.pentyugov.wflow.core.service.ChatMessageService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(ChatMessageService.NAME)
@RequiredArgsConstructor
public class ChatMessageServiceImpl extends AbstractService implements ChatMessageService {

    private  final EntityManager entityManager;
    private final ChatRepository chatRepository;
    private final UserService userService;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return this.chatRepository.save(chatMessage);
    }

    @Override
    public ChatMessage update(ChatMessageDto chatMessageDto) throws UserNotFoundException {
        ChatMessage chatMessage = entityManager.merge(convert(chatMessageDto));
        entityManager.flush();
        return chatMessage;
    }

    @Override
    public ChatMessage convert(ChatMessageDto chatMessageDto) throws UserNotFoundException {
        ChatMessage chatMessage = chatRepository.findById(chatMessageDto.getId()).orElse(new ChatMessage());
        chatMessage.setChatId(chatMessageDto.getChatId());
        chatMessage.setContent(chatMessageDto.getContent());
        chatMessage.setStatus(chatMessageDto.getStatus());
        chatMessage.setSender(userService.getById(UUID.fromString(chatMessageDto.getSenderId())));
        chatMessage.setRecipient(userService.getById(UUID.fromString(chatMessageDto.getRecipientId())));
        return chatMessage;
    }

    @Override
    public ChatMessageDto convert(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .status(chatMessage.getStatus())
                .chatId(chatMessage.getChatId())
                .createDate(Date.from(chatMessage.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()))
                .senderId(chatMessage.getSender().getId().toString())
                .sender(userService.convert(chatMessage.getSender()))
                .recipientId(chatMessage.getRecipient().getId().toString())
                .recipient(userService.convert(chatMessage.getRecipient()))
                .build();
    }

    @Override
    public List<ChatMessage> getChatMessagesByChatId(String chatId) {
        return chatRepository.findByChatId(chatId);
    }

    @Override
    public Page<ChatMessage> getPageChatMessagesByChatId(String chatId, Optional<Integer> page, Optional<String> sortBy) {
        return chatRepository.findByChatId(chatId, PageRequest.of(
                page.orElse(0),
                10,
                Sort.Direction.DESC, sortBy.orElse("createDate")));
    }

    @Override
    public Integer getNewChatMessagesCountForUser(UUID userId) {
        return chatRepository.findNewMessagesCountForUser(userId, ChatMessage.READ);
    }

    @Override
    public List<ChatMessageDto> getChatMessagesForUserWithStatus(UUID userId, int status) {
        return  chatRepository.findMessagesForUserByStatus(userId, status)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageDto> getNewMessagesForUser(UUID userId) {
        return chatRepository.findNewMessagesForUser(userId, ChatMessage.READ)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessage getById(UUID uuid) {
        return chatRepository.getById(uuid);
    }

}