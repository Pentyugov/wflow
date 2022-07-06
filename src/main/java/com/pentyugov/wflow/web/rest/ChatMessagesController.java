package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.ChatMessage;
import com.pentyugov.wflow.core.domain.entity.ChatRoom;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.dto.ChatMessageDto;
import com.pentyugov.wflow.core.service.ChatMessageService;
import com.pentyugov.wflow.core.service.ChatRoomService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.core.service.UserWsSessionService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.payload.response.ChatMessagePageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/chat-messages")
public class ChatMessagesController extends AbstractController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final UserWsSessionService userWsSessionService;

    @Autowired
    public ChatMessagesController(ChatMessageService chatMessageService, ChatRoomService chatRoomService, UserService userService, UserWsSessionService userWsSessionService) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.userWsSessionService = userWsSessionService;
    }

    @GetMapping("/get-room-messages/{recipientId}")
    public ResponseEntity<Object> getChatRoomMessages(@PathVariable String recipientId, Principal principal) throws UserNotFoundException {
        User sender = userService.getUserByPrincipal(principal);
        String chatId = chatRoomService.getChatId(sender.getId(), UUID.fromString(recipientId), false).orElse(null);
        if (StringUtils.hasText(chatId)) {
            List<ChatMessageDto> result = new ArrayList<>();
            chatMessageService.getChatMessagesByChatId(chatId).forEach(chatMessage -> result.add(chatMessageService.createProxyFromChatMessage(chatMessage)));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-user-chat-messages-map")
    public ResponseEntity<Object> getUserChatMessagesMap(Principal principal) throws UserNotFoundException {
        User current = userService.getUserByPrincipal(principal);
        List<ChatRoom> rooms = chatRoomService.getRoomsForUser(current);

        Map<String, ChatMessagePageResponse> result = new HashMap<>();
        for (ChatRoom chatRoom : rooms) {
            ChatMessagePageResponse chatMessagePageResponse = new ChatMessagePageResponse();
            List<ChatMessageDto> messages = new ArrayList<>();
            Page<ChatMessage> page = chatMessageService.getPageChatMessagesByChatId(chatRoom.getChatId(), Optional.of(0), Optional.empty());
            chatMessagePageResponse.setTotalPages(page.getTotalPages());
            page.forEach(chatMessage ->
                    messages.add(chatMessageService.createProxyFromChatMessage(chatMessage)));
            Collections.reverse(messages);
            chatMessagePageResponse.setMessages(messages);
            result.put(chatRoom.getRecipient().getId().toString(), chatMessagePageResponse);
        }


        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-user-chat-messages-map-page")
    public List<ChatMessageDto> getUserChatMessagesMapPage(@RequestParam Optional<Integer> page,
                                                           @RequestParam String chatId,
                                                           @RequestParam Optional<String> sortBy) {
        List<ChatMessageDto> result = new ArrayList<>();
        Page<ChatMessage> pageChatMessagesByChatId = chatMessageService.getPageChatMessagesByChatId(chatId, page, sortBy);
        pageChatMessagesByChatId.getContent().forEach(message ->
                result.add(chatMessageService.createProxyFromChatMessage(message)));
        Collections.reverse(result);
        return result;
    }


    @GetMapping("/get-user-chat-status-map")
    public ResponseEntity<Object> getUserChatStatusMap() {
        Map<UUID, Integer> result = new HashMap<>();
        userService.getAllUsers().forEach(user -> {
            if (userWsSessionService.isUserOnline(user.getId().toString())) {
                result.put(user.getId(), 20);
            } else {
                result.put(user.getId(), 10);
            }
        });

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-new-messages-count")
    public ResponseEntity<Object> getNewMessagesCount(Principal principal) throws UserNotFoundException {
        User user = userService.getUserByPrincipal(principal);
        Integer count = chatMessageService.getNewChatMessagesCountForUser(user.getId());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/get-messages-for-current-user")
    public ResponseEntity<Object> getMessagesForUserByStatus(Principal principal, @RequestParam Integer status) throws UserNotFoundException {
        User user = userService.getUserByPrincipal(principal);
        return new ResponseEntity<>(
                chatMessageService.getChatMessagesForUserWithStatus(user.getId(), status),
                HttpStatus.OK
        );

    }

    @GetMapping("/get-new-messages-for-current-user")
    public ResponseEntity<Object> getNewMessagesForUser(Principal principal) throws UserNotFoundException {
        User user = userService.getUserByPrincipal(principal);
        return new ResponseEntity<>(
                chatMessageService.getNewMessagesForUser(user.getId()),
                HttpStatus.OK
        );
    }


}
