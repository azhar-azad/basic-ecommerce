package com.azad.basicecommerce.model.auth;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {

    private String username;
    private String email;

    @NotNull(message = "Password cannot be empty")
    private String password;
}
