package com.ehizman.goodreads.controllers;

import com.ehizman.goodreads.controllers.requestsAndResponses.ApiResponse;
import com.ehizman.goodreads.controllers.requestsAndResponses.AccountCreationRequest;
import com.ehizman.goodreads.controllers.requestsAndResponses.UpdateRequest;
import com.ehizman.goodreads.dtos.UserDto;
import com.ehizman.goodreads.exceptions.GoodReadsException;
import com.ehizman.goodreads.services.UserService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody @Valid @NotNull AccountCreationRequest accountCreationRequest){
        try {
            log.info("Account Creation Request ==> {}",accountCreationRequest);
            UserDto userDto = userService.createUserAccount(accountCreationRequest);
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("success")
                    .message("user created successfully")
                    .data(userDto)
                    .build();
            log.info("Returning response");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        catch (GoodReadsException e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("fail")
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(e.getStatusCode()));
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") @NotNull @NotBlank String userId) {
        try {
            if (("null").equals(userId) || ("").equals(userId.trim())){
                throw new GoodReadsException("String id cannot be null", 400);
            }
            UserDto userDto = userService.findUserById(userId);
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("success")
                    .message("user found")
                    .data(userDto)
                    .result(1)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (GoodReadsException e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("fail")
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(e.getStatusCode()));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(){
        List<UserDto> users = userService.findAll();
        ApiResponse apiResponse = ApiResponse.builder()
                .status("success")
                .message(users.size() != 0 ? "users found" : "no user exists in database")
                .data(users)
                .result(users.size())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PatchMapping("/")
    public ResponseEntity<?> updateUserProfile(@Valid @NotBlank @NotNull @RequestParam String id,
                                               @RequestBody @NotNull UpdateRequest updateRequest ) {

        try {
            UserDto userDto = userService.updateUserProfile(id, updateRequest);
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("success")
                    .message("user found")
                    .data(userDto)
                    .result(1)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (GoodReadsException e) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .status("fail")
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(e.getStatusCode()));
        }
    }
}
