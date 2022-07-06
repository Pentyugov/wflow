package com.pentyugov.wflow.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = "sys$Mail")
@Table(name = "SYSTEM_MAIL")
@Where(clause="DELETE_DATE is null")
public class SysMail extends BaseEntity {

    @Column(name = "SENDER")
    private String sender;

    @Column(name = "RECEIVER")
    private String receiver;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "BODY", columnDefinition="TEXT")
    private String body;

    @Column(name = "SEND_TIME")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @Column(name = "IS_SEND")
    private Boolean isSend;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public Boolean isSend() {
        return isSend;
    }

    public void setIsSend(Boolean send) {
        isSend = send;
    }
}
