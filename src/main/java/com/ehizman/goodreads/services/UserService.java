package com.ehizman.goodreads.services;

import com.ehizman.goodreads.dtos.AccountCreationRequest;
import com.ehizman.goodreads.dtos.UserDto;
import com.ehizman.goodreads.exceptions.GoodReadsException;

public interface UserService {
    UserDto createUserAccount(AccountCreationRequest accountCreationRequest) throws GoodReadsException;
}
