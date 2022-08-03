package com.ehizman.goodreads.services;

import com.ehizman.goodreads.controllers.requestsAndResponses.AccountCreationRequest;
import com.ehizman.goodreads.controllers.requestsAndResponses.UpdateRequest;
import com.ehizman.goodreads.dtos.UserDto;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.models.User;
import com.ehizman.goodreads.respositories.UserRepository;
import com.ehizman.goodreads.utils.ModelMapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.modelMapper = mapper;
    }

    @Override
    public UserDto createUserAccount(AccountCreationRequest accountCreationRequest) throws GoodReadsException {
        log.info("In Create User Account Method");
        validate(accountCreationRequest, userRepository);
        log.info("After Validated Method");
        User user = User.builder()
                .firstName(accountCreationRequest.getFirstName())
                .lastName(accountCreationRequest.getLastName())
                .email(accountCreationRequest.getEmail())
                .password(accountCreationRequest.getPassword())
                .dateJoined(LocalDate.now())
                .build();
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto  findUserById(String userId) throws GoodReadsException {
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(
                () -> new GoodReadsException(String.format("User with id %s not found", userId), 404)
        );
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
    }

    @Override
    public UserDto updateUserProfile(String id, UpdateRequest updateRequest) throws GoodReadsException {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(
                () -> new GoodReadsException("user id not found", 404)
        );
        User userToSave = modelMapper.map(updateRequest,User.class);
        userToSave.setId(user.getId());
        userToSave.setDateJoined(user.getDateJoined());
        userRepository.save(userToSave);
        return modelMapper.map(userToSave, UserDto.class);
    }

    private static void validate(AccountCreationRequest accountCreationRequest, UserRepository userRepository) throws GoodReadsException {

        User user = userRepository.findUserByEmail(accountCreationRequest.getEmail()).orElse(null);
        if (user != null){
            throw new GoodReadsException("user email already exists", 400);
        }
    }
}
