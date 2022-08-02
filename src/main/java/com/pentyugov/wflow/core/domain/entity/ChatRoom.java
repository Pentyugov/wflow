package com.pentyugov.wflow.core.domain.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
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

}
