package com.pentyugov.wflow.core.domain.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity(name = "workflow$ChatMessage")
@Table(name = "WORKFLOW_CHAT_MESSAGE")
@Where(clause="DELETE_DATE is null")
public class ChatMessage extends BaseEntity {

    public static final int SEND = 10;
    public static final int RECEIVED = 20;
    public static final int READ = 30;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CHAT_ID")
    private String chatId;

    @Column(name = "STATUS")
    private int status;

    @ManyToOne
    @JoinColumn(name = "SENDER_ID")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "RECIPIENT_ID")
    private User recipient;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
