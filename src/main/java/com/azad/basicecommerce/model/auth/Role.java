package com.azad.basicecommerce.model.auth;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Role {

    @NotNull(message = "Role name cannot be empty")
    protected String roleName;
}
