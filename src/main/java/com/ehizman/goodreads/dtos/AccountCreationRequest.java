package com.ehizman.goodreads.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
