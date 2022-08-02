package com.ehizman.goodreads.utils;

import com.ehizman.goodreads.controllers.requestsAndResponses.AccountCreationRequest;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.models.User;
import com.ehizman.goodreads.respositories.UserRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AccountValidation {
    public static void validate(AccountCreationRequest accountCreationRequest, UserRepository userRepository) throws GoodReadsException {
        log.info("In validate Method");

        User user = userRepository.findUserByEmail(accountCreationRequest.getEmail()).orElse(null);
        if (user != null){
            throw new GoodReadsException("user email already exists", 400);
        }
    }
}
