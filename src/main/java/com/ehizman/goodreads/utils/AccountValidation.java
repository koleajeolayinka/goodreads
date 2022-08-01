package com.ehizman.goodreads.utils;

import com.ehizman.goodreads.dtos.AccountCreationRequest;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.models.User;
import com.ehizman.goodreads.respositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountValidation {

    private static UserRepository userRepository;

    public AccountValidation(UserRepository userRepository) {
        AccountValidation.userRepository = userRepository;
    }

    public static void validate(AccountCreationRequest accountCreationRequest) throws GoodReadsException {
        User user = userRepository.findUserByEmail(accountCreationRequest.getEmail()).orElse(null);
        if (user != null){
            throw new GoodReadsException("user email already exists");
        }
    }
}
