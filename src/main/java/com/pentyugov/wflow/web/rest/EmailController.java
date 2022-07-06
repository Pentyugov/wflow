package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.SysMail;
import com.pentyugov.wflow.core.service.EmailService;
import com.pentyugov.wflow.core.service.MessageService;
import com.pentyugov.wflow.web.payload.request.SendEmailRequest;
import com.pentyugov.wflow.web.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("/api/service/mail/")
public class EmailController {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);


    private final EmailService emailService;
    private final MessageService messageService;

    @Autowired
    public EmailController(EmailService emailService, MessageService messageService) {
        this.emailService = emailService;
        this.messageService = messageService;
    }

    @Value("${source.service.email}")
    private String resourcePath;

    @Value("${spring.mail.username}")
    private String sender;

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendEmail(@Valid @RequestBody SendEmailRequest sendEmailRequest,
                                                     Locale locale) {
        String result;
        LOGGER.info("Sending mail to {}", sendEmailRequest.getReceiver());
        SysMail sysMail = emailService.createSysMail(sender,
                sendEmailRequest.getReceiver(),
                sendEmailRequest.getSubject(),
                sendEmailRequest.getBody());
        try {
            emailService.sendSimpleMessage(sysMail);

            sysMail.setIsSend(true);
            sysMail.setSendTime(LocalDateTime.now());

            result = String.format(messageService.getMessage(resourcePath, "mail.send.success", locale),
                    sendEmailRequest.getReceiver());

        } catch (MailException mailException) {
            LOGGER.error(mailException.getMessage());
            result = "Error while send mail, see logs for details";
        }
        emailService.saveSysMail(sysMail);

        return new ResponseEntity<>(new MessageResponse(result), HttpStatus.OK);
    }

    @PostMapping("/send/html")
    @PreAuthorize("hasAuthority('SEND_SYS_MAIL')")
    public ResponseEntity<MessageResponse> sendEmailWithHtml(@Valid @RequestBody SendEmailRequest sendEmailRequest,
                                                             Locale locale) {

        String result;

        LOGGER.info("Sending mail to {}", sendEmailRequest.getReceiver());
        SysMail sysMail = emailService.createSysMail(sender,
                sendEmailRequest.getReceiver(),
                sendEmailRequest.getSubject(),
                sendEmailRequest.getBody());
        try {
            emailService.sendMimeMessage(sysMail);

            sysMail.setIsSend(true);
            sysMail.setSendTime(LocalDateTime.now());

            result = String.format(messageService.getMessage(resourcePath, "mail.send.success", locale),
                    sendEmailRequest.getReceiver());

        } catch (MailException | MessagingException mailException) {
            LOGGER.error(mailException.getMessage());
            result = "Error while send mail, see logs for details";
        }

        emailService.saveSysMail(sysMail);

        return new ResponseEntity<>(new MessageResponse(result), HttpStatus.OK);
    }



}
