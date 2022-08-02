package com.ehizman.goodreads.services;

import com.ehizman.goodreads.controllers.requestsAndResponses.AccountCreationRequest;
import com.ehizman.goodreads.controllers.requestsAndResponses.UpdateRequest;
import com.ehizman.goodreads.dtos.UserDto;
import com.ehizman.goodreads.exceptions.GoodReadsException;

import java.util.List;

public interface UserService {
    UserDto createUserAccount(AccountCreationRequest accountCreationRequest) throws GoodReadsException;
    UserDto findUserById(String userId) throws GoodReadsException;
    List<UserDto> findAll();
    UserDto updateUserProfile(String id, UpdateRequest updateRequest) throws GoodReadsException;
}
