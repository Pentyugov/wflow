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
    public ChatMessage updateMessage(ChatMessageDto chatMessageDto) throws UserNotFoundException {
        ChatMessage chatMessage = entityManager.merge(createChatMessageFromProxy(chatMessageDto));
        entityManager.flush();
        return chatMessage;
    }

    @Override
    public ChatMessage createChatMessageFromProxy(ChatMessageDto chatMessageDto) throws UserNotFoundException {
        ChatMessage chatMessage = new ChatMessage();
        if (chatMessageDto.getId() != null) {
            chatMessage = entityManager.find(ChatMessage.class, chatMessageDto.getId());
        }
        chatMessage.setChatId(chatMessageDto.getChatId());
        chatMessage.setContent(chatMessageDto.getContent());
        chatMessage.setStatus(chatMessageDto.getStatus());
        chatMessage.setSender(userService.getUserById(UUID.fromString(chatMessageDto.getSenderId())));
        chatMessage.setRecipient(userService.getUserById(UUID.fromString(chatMessageDto.getRecipientId())));
        return chatMessage;
    }

    @Override
    public ChatMessageDto createProxyFromChatMessage(ChatMessage chatMessage) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setId(chatMessage.getId());
        chatMessageDto.setContent(chatMessage.getContent());
        chatMessageDto.setStatus(chatMessage.getStatus());
        chatMessageDto.setChatId(chatMessage.getChatId());
        chatMessageDto.setCreateDate(Date.from(chatMessage.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()));
        chatMessageDto.setSenderId(chatMessage.getSender().getId().toString());
        chatMessageDto.setSender(userService.createUserDtoFromUser(chatMessage.getSender()));
        chatMessageDto.setRecipientId(chatMessage.getRecipient().getId().toString());
        chatMessageDto.setRecipient(userService.createUserDtoFromUser(chatMessage.getRecipient()));
        return chatMessageDto;
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
                .map(this::createProxyFromChatMessage)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageDto> getNewMessagesForUser(UUID userId) {
        return chatRepository.findNewMessagesForUser(userId, ChatMessage.READ)
                .stream()
                .map(this::createProxyFromChatMessage)
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessage getById(UUID uuid) {
        return chatRepository.getById(uuid);
    }

}