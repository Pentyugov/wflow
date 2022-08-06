package com.pentyugov.wflow.core.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "workflow$ChatMessage")
@Table(name = "WORKFLOW_CHAT_MESSAGE")
@Where(clause = "DELETE_DATE is null")
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

}
