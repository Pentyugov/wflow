package com.pentyugov.wflow.web.payload.response;

import com.pentyugov.wflow.core.dto.ChatMessageDto;

import java.util.List;

public class ChatMessagePageResponse {

    private int totalPages;
    private List<ChatMessageDto> messages;


    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ChatMessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageDto> messages) {
        this.messages = messages;
    }
}
