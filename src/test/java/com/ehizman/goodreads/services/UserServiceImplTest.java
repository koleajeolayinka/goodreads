package com.ehizman.goodreads.services;

import com.ehizman.goodreads.controllers.requestsAndResponses.AccountCreationRequest;
import com.ehizman.goodreads.dtos.UserDto;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.models.User;
import com.ehizman.goodreads.respositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private ModelMapper mapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, mapper, emailService);
    }

    @Test
    void testThatUserCanCreateAccount() throws GoodReadsException {
        AccountCreationRequest accountCreationRequest =
                new AccountCreationRequest("Firstname", "Lastname", "testemail@gmail.com","password" );
        UserDto userDto = userService.createUserAccount(accountCreationRequest);

        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        assertThat(optionalUser.get().getFirstName()).isEqualTo("Firstname");
        assertThat(optionalUser.get().getLastName()).isEqualTo("Lastname");
        assertThat(optionalUser.get().getEmail()).isEqualTo("testemail@gmail.com");
        assertThat(optionalUser.get().getPassword()).isEqualTo("password");
    }

    @Test
    void testThatUserEmailIsUnique() throws GoodReadsException {
        AccountCreationRequest firstAccountCreationRequest =
                new AccountCreationRequest("Firstname", "Lastname", "testemail@gmail.com","password" );
        UserDto userDto = userService.createUserAccount(firstAccountCreationRequest);

        AccountCreationRequest secondAccountCreationRequest =
                new AccountCreationRequest("Amaka", "Chopper", "testemail@gmail.com","password1234" );
        assertThrows(GoodReadsException.class, ()-> userService.createUserAccount(secondAccountCreationRequest));
    }


}