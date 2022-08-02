package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.SysMail;
import com.pentyugov.wflow.core.domain.entity.User;
import com.pentyugov.wflow.core.repository.SysMailRepository;
import com.pentyugov.wflow.core.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(EmailService.NAME)
@RequiredArgsConstructor
public class EmailServiceImpl extends AbstractService implements EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    public String sender;
    @Value("${source.service.email}")
    public String messagesPath;

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final SysMailRepository sysMailRepository;

    @Async
    public void sendSimpleMessage(SysMail sysMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sysMail.getSender());
        message.setTo(sysMail.getReceiver());
        message.setSubject(sysMail.getSubject());
        message.setText(sysMail.getBody());
        emailSender.send(message);
    }

    @Async
    public void sendMimeMessage(SysMail sysMail) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.setSubject(sysMail.getSubject());
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sysMail.getSender());
        helper.setTo(sysMail.getReceiver());
        helper.setText(sysMail.getBody(), true);
        emailSender.send(message);
    }

    public SysMail createSysMail(String sender, String receiver, String subject, String body) {
        SysMail sysMail = new SysMail();
        sysMail.setSender(sender);
        sysMail.setReceiver(receiver);
        sysMail.setSubject(subject);
        sysMail.setBody(body);
        return sysMail;
    }

    public void saveSysMail(SysMail sysMail) {
        sysMailRepository.save(sysMail);
    }

    @Async
    public void  resendEmails() {
        List<SysMail> notSent = getNotSendEmails();
        if (!CollectionUtils.isEmpty(notSent)) {
            notSent.forEach(sysMail -> {
                boolean isSent = false;

                try {
                    sendMimeMessage(sysMail);
                    isSent = true;
                } catch (AddressException e) {
                    LOGGER.error(e.getMessage());
                    isSent = false;
                    sysMail.setIsSend(false);
                    sysMailRepository.delete(sysMail);
                } catch (MessagingException e) {
                    LOGGER.error(e.getMessage());
                }

                if (isSent) {
                    sysMail.setSendTime(LocalDateTime.now());
                    sysMail.setIsSend(true);
                    sysMailRepository.save(sysMail);
                    LOGGER.info("System mail to {} was successfully sent", sysMail.getReceiver());
                }
            });
        }
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom(sender);
        emailSender.send(message);
    }

    @Async
    public void sendRegisterSuccessMail(User user, String rawPassword) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipient", user.getFullName());
        templateModel.put("login", user.getUsername());
        templateModel.put("password", rawPassword);

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String subject = messageService.getMessage(messagesPath, "subject");
        String htmlBody = templateEngine.process("register-mail.html", thymeleafContext);

        SysMail sysMail = createSysMail(sender, user.getEmail(), subject, htmlBody);

        try {
            LOGGER.info("Sending mail about registration to {}", user.getEmail());
            sendHtmlMessage(user.getEmail(), subject, htmlBody);
            sysMail.setSendTime(LocalDateTime.now());
            sysMail.setIsSend(true);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }

        saveSysMail(sysMail);

    }

    @Async
    public void sentResetPasswordMail(User user, String rawPassword) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipient", user.getFullName());
        templateModel.put("password", rawPassword);

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String subject = messageService.getMessage(messagesPath, "resetPswSubject");
        String htmlBody = templateEngine.process("reset-password-mail.html", thymeleafContext);

        SysMail sysMail = createSysMail(sender, user.getEmail(), subject, htmlBody);

        try {
            LOGGER.info("Sending mail about password reset to {}", user.getEmail());
            sendHtmlMessage(user.getEmail(), subject, htmlBody);
            sysMail.setSendTime(LocalDateTime.now());
            sysMail.setIsSend(true);
        } catch (MessagingException exception) {
            LOGGER.error(exception.getMessage());
        }

        saveSysMail(sysMail);
    }

    @Async
    public void sentTelegramVerificationCodeMail(User user, String code) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipient", user.getFullName());
        templateModel.put("code", code);

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String subject = messageService.getMessage(messagesPath, "verificationCodeSubject");
        String htmlBody = templateEngine.process("verification-code.html", thymeleafContext);

        SysMail sysMail = createSysMail(sender, user.getEmail(), subject, htmlBody);

        try {
            LOGGER.info("Sending mail with telegram verification code to {}", user.getEmail());
            sendHtmlMessage(user.getEmail(), subject, htmlBody);
            sysMail.setSendTime(LocalDateTime.now());
            sysMail.setIsSend(true);
        } catch (MessagingException exception) {
            LOGGER.error(exception.getMessage());
        }

        saveSysMail(sysMail);
    }

    private List<SysMail> getNotSendEmails() {
        return sysMailRepository.getNotSent();
    }
}
