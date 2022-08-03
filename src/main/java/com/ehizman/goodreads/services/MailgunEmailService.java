package com.ehizman.goodreads.services;

import com.ehizman.goodreads.models.MessageRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@NoArgsConstructor
@Slf4j
public class MailgunEmailService implements EmailService{
    private final String DOMAIN = System.getenv("DOMAIN");
    private final String PRIVATE_KEY = System.getenv("MAILGUN_PRIVATE_KEY");

    @Override
    @Async
    public CompletableFuture<JsonNode> sendSimpleMail(MessageRequest messageRequest) throws UnirestException {
        log.info("DOMAIN -> {}", DOMAIN);
        log.info("API KEY -> {}", PRIVATE_KEY);
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
			.basicAuth("api", PRIVATE_KEY)
                .queryString("from", messageRequest.getSender())
                .queryString("to", messageRequest.getReceiver())
                .queryString("subject", messageRequest.getSubject())
                .queryString("text", messageRequest.getBody())
                .asJson();
        log.info("Finished sending email");
        return CompletableFuture.completedFuture(request.getBody());
    }

    @Override
    public void sendHtmlMail(MessageRequest messageRequest) {
    }
}
