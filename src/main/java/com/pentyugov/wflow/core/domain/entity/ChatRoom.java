package com.pentyugov.wflow.core.domain.entity;


import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$ChatRoom")
@Table(name = "WORKFLOW_CHAT_Room")
@Where(clause="DELETE_DATE is null")
public class ChatRoom extends BaseEntity {

    @Column(name = "CHAT_ID")
    private String chatId;
    @ManyToOne
    @JoinColumn(name = "SENDER_ID")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "RECIPIENT_ID")
    private User recipient;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
