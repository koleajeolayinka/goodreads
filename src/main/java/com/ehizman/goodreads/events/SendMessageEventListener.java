package com.ehizman.goodreads.events;

import com.ehizman.goodreads.models.MailResponse;
import com.ehizman.goodreads.models.MessageRequest;
import com.ehizman.goodreads.services.EmailService;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class SendMessageEventListener {
    @Qualifier("mailgun_sender")
    @Autowired
    EmailService emailService;

    @EventListener
    public void handleSendMessageEvent(SendMessageEvent event) throws UnirestException, ExecutionException, InterruptedException {
        MailResponse mailResponse = emailService.sendSimpleMail((MessageRequest) event.getSource()).get();
    }
}
