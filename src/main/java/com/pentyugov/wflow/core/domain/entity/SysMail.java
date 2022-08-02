package com.pentyugov.wflow.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

}
