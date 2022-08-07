package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.ChatMessage;
import com.pentyugov.wflow.core.domain.entity.ChatRoom;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.ChatMessageDto;
import com.pentyugov.wflow.core.service.*;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.response.ChatMessagePageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat-messages")
@RequiredArgsConstructor
public class ChatMessagesController extends AbstractController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final UserWsSessionService userWsSessionService;
    private final UserSessionService userSessionService;

    @GetMapping("/get-room-messages/{recipientId}")
    public ResponseEntity<Object> getChatRoomMessages(@PathVariable String recipientId) throws UserNotFoundException {
        User sender = userSessionService.getCurrentUser();
        String chatId = chatRoomService.getChatId(sender.getId(), UUID.fromString(recipientId), false)
                .orElse(null);
        if (StringUtils.hasText(chatId)) {
            List<ChatMessageDto> result = new ArrayList<>();
            chatMessageService.getChatMessagesByChatId(chatId)
                    .forEach(chatMessage -> result.add(chatMessageService.convert(chatMessage)));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-user-chat-messages-map")
    public ResponseEntity<Object> getUserChatMessagesMap() {
        User current = userSessionService.getCurrentUser();
        List<ChatRoom> rooms = chatRoomService.getRoomsForUser(current);
        Map<String, ChatMessagePageResponse> result = new HashMap<>();
        for (ChatRoom chatRoom : rooms) {
            ChatMessagePageResponse response = new ChatMessagePageResponse();
            List<ChatMessageDto> messages = new ArrayList<>();
            Page<ChatMessage> page = chatMessageService.getPageChatMessagesByChatId(chatRoom.getChatId(), Optional.of(0), Optional.empty());
            response.setTotalPages(page.getTotalPages());
            page.forEach(chatMessage ->
                    messages.add(chatMessageService.convert(chatMessage)));
            Collections.reverse(messages);
            response.setMessages(messages);
            result.put(chatRoom.getRecipient().getId().toString(), response);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-user-chat-messages-map-page")
    public List<ChatMessageDto> getUserChatMessagesMapPage(@RequestParam Optional<Integer> page,
                                                           @RequestParam String chatId,
                                                           @RequestParam Optional<String> sortBy) {
        List<ChatMessageDto> result = new ArrayList<>();
        Page<ChatMessage> chatPage = chatMessageService.getPageChatMessagesByChatId(chatId, page, sortBy);
        chatPage.getContent().forEach(message ->
                result.add(chatMessageService.convert(message)));
        Collections.reverse(result);
        return result;
    }


    @GetMapping("/get-user-chat-status-map")
    public ResponseEntity<Object> getUserChatStatusMap() {
        Map<UUID, Integer> result = new HashMap<>();
        userService.getAll().forEach(user -> {
            if (userWsSessionService.isUserOnline(user.getId().toString())) {
                result.put(user.getId(), 20);
            } else {
                result.put(user.getId(), 10);
            }
        });

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-new-messages-count")
    public ResponseEntity<Object> getNewMessagesCount() {
        Integer count = chatMessageService.getNewChatMessagesCountForUser(userSessionService.getCurrentUser().getId());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/get-messages-for-current-user")
    public ResponseEntity<Object> getMessagesForUserByStatus(@RequestParam Integer status) {

        return new ResponseEntity<>(
                chatMessageService.getChatMessagesForUserWithStatus(userSessionService.getCurrentUser().getId(), status),
                HttpStatus.OK
        );

    }

    @GetMapping("/get-new-messages-for-current-user")
    public ResponseEntity<Object> getNewMessagesForUser() {
        return new ResponseEntity<>(
                chatMessageService.getNewMessagesForUser(userSessionService.getCurrentUser().getId()),
                HttpStatus.OK
        );
    }


}
