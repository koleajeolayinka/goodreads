package com.ehizman.goodreads.services.mockTests;

import com.ehizman.goodreads.controllers.requestsAndResponses.AccountCreationRequest;
import com.ehizman.goodreads.dtos.UserDto;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.models.User;
import com.ehizman.goodreads.respositories.UserRepository;
import com.ehizman.goodreads.services.EmailService;
import com.ehizman.goodreads.services.UserService;
import com.ehizman.goodreads.services.UserServiceImpl;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceMockTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    private UserService userService;
    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private ModelMapper mapper;

//    @Captor
//    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, mapper, emailService, applicationEventPublisher);
    }

    @Test
    void testThatUserCanCreateAccount() throws GoodReadsException, UnirestException, ExecutionException, InterruptedException {
        AccountCreationRequest accountCreationRequest =
                new AccountCreationRequest("Firstname", "Lastname", "testemail@gmail.com","password" );

        User userToReturn = User.builder()
                .id(1L)
                .firstName(accountCreationRequest.getFirstName())
                .lastName(accountCreationRequest.getLastName())
                .email(accountCreationRequest.getEmail())
                .password(accountCreationRequest.getPassword())
                .build();
        UserDto userDtoToReturn = UserDto.builder()
                .id(1L)
                .firstName(accountCreationRequest.getFirstName())
                .lastName(accountCreationRequest.getLastName())
                .email(accountCreationRequest.getEmail())
                .build();
        when(userRepository.findUserByEmail("testemail@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userToReturn);
        when(mapper.map(userToReturn, UserDto.class)).thenReturn(userDtoToReturn);
        UserDto userDto = userService.createUserAccount(accountCreationRequest);
        verify(userRepository, times(1)).findUserByEmail("testemail@gmail.com");
//        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

//        User capturedUser = userArgumentCaptor.getValue();

//        log.info("Captured user -> {}", capturedUser);

//        assertThat(capturedUser.findUserById()).isEqualTo(1L);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getFirstName()).isEqualTo("Firstname");
        assertThat(userDto.getLastName()).isEqualTo("Lastname");
        assertThat(userDto.getEmail()).isEqualTo("testemail@gmail.com");

    }

    @Test
    void testThatUserEmailIsUnique() throws GoodReadsException, UnirestException, ExecutionException, InterruptedException {
        AccountCreationRequest accountCreationRequest =
                new AccountCreationRequest("Firstname", "Lastname", "testemail@gmail.com","password" );

        User userToReturn = User.builder()
                .id(1L)
                .firstName(accountCreationRequest.getFirstName())
                .lastName(accountCreationRequest.getLastName())
                .email(accountCreationRequest.getEmail())
                .password(accountCreationRequest.getPassword())
                .build();
        when(userRepository.findUserByEmail("testemail@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userToReturn);
        userService.createUserAccount(accountCreationRequest);

        verify(userRepository, times(1)).findUserByEmail("testemail@gmail.com");

        AccountCreationRequest secondCreationRequest =
                new AccountCreationRequest("Ladi", "Akinson", "testemail@gmail.com","password" );

        when(userRepository.findUserByEmail("testemail@gmail.com")).thenReturn(Optional.of(userToReturn));
        assertThatThrownBy(()->userService.createUserAccount(accountCreationRequest))
                .isInstanceOf(GoodReadsException.class)
                .hasMessage("user email already exists");

        verify(userRepository, times(2)).findUserByEmail("testemail@gmail.com");
    }

    @Test
    void testThatCanFindUserById() throws GoodReadsException {
        String userId = "1";
        User user = User.builder()
                        .id(1L)
                        .firstName("Amaka")
                        .lastName("Azubuike")
                        .email("amyAzu@gmail.com")
                        .password("Amaka_2005")
                        .build();

        UserDto userDtoToReturn = UserDto.builder()
                .id(1L)
                .firstName("Amaka")
                .lastName("Azubuike")
                .email("amyAzu@gmail.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.map(user, UserDto.class)).thenReturn(userDtoToReturn);

        UserDto userDto = userService.findUserById(userId);
        verify(userRepository, times(1)).findById(1L);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getFirstName()).isEqualTo("Amaka");
        assertThat(userDto.getLastName()).isEqualTo("Azubuike");
        assertThat(userDto.getEmail()).isEqualTo("amyAzu@gmail.com");
    }
}
