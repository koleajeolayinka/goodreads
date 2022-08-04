package com.ehizman.goodreads.events;

import com.ehizman.goodreads.models.MailResponse;
import com.ehizman.goodreads.models.MessageRequest;
import com.ehizman.goodreads.services.EmailService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.ExecutionException;

@Component
public class SendMessageEventListener {
    @Qualifier("mailgun_sender")
    @Autowired
    private EmailService emailService;
    @Autowired
    private TemplateEngine templateEngine;

    @EventListener
    public void handleSendMessageEvent(SendMessageEvent event) throws UnirestException, ExecutionException, InterruptedException {
        MessageRequest messageRequest = (MessageRequest) event.getSource();
        Context context = new Context();
        context.setVariable("user_name", messageRequest.getUsersFullName());
        context.setVariable("verification_token", "https://www.google.com");
        messageRequest.setBody(templateEngine.process("registration_verification_mail.html", context));
        MailResponse mailResponse = emailService.sendSimpleMail((MessageRequest) event.getSource()).get();
    }
}
