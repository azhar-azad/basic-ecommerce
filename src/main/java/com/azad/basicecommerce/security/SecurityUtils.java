package com.azad.basicecommerce.security;

import com.azad.basicecommerce.common.exceptions.UserNotFoundException;
import com.azad.basicecommerce.model.auth.AppUserEntity;
import com.azad.basicecommerce.repository.AppUserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityUtils {

    @Value("${auth_base}")
    private String authBase;

    @Autowired
    private AppUserRepository repository;

    public boolean isUsernameBasedAuth() {
        return authBase.equalsIgnoreCase("USERNAME");
    }

    public boolean isEmailBasedAuth() {
        return authBase.equalsIgnoreCase("EMAIL");
    }

    public AppUserEntity getUserByAuthBase(String usernameOrEmail) throws RuntimeException {
        if (isUsernameBasedAuth())
            return repository.findByUsername(usernameOrEmail).orElseThrow(
                    () -> new UserNotFoundException("Unregistered User", "Identifier: " + usernameOrEmail));
        else if (isEmailBasedAuth())
            return repository.findByEmail(usernameOrEmail).orElseThrow(
                    () -> new UserNotFoundException("Unregistered User", "Identifier: " + usernameOrEmail));
        else
            throw new RuntimeException("Unknown Authentication base configured. Valid auth_base values are USERNAME or EMAIL");
    }
}
