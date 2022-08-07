package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.SysMail;
import com.pentyugov.wflow.core.domain.entity.User;

import javax.mail.MessagingException;


public interface EmailService {

    String NAME = "wflow$EmailService";

    void sendSimpleMessage(SysMail sysMail);

    void sendMimeMessage(SysMail sysMail) throws MessagingException;

    SysMail createSysMail(String sender, String receiver, String subject, String body);

    void saveSysMail(SysMail sysMail);

    void resendEmails();

    void sendRegisterSuccessMail(User user, String rawPassword);

    void sentResetPasswordMail(User user, String rawPassword);

    void sentTelegramVerificationCodeMail(User user, String code);


}
