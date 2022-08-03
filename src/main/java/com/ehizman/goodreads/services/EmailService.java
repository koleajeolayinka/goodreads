package com.ehizman.goodreads.services;

import com.ehizman.goodreads.models.MailResponse;
import com.ehizman.goodreads.models.MessageRequest;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<MailResponse> sendSimpleMail(MessageRequest messageRequest) throws UnirestException;
    void sendHtmlMail(MessageRequest messageRequest);
}
