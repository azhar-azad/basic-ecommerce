package com.azad.basicecommerce.model.auth;

import com.azad.basicecommerce.model.address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AppUserResponse extends AppUser {

    private Role role;
    private List<Address> addresses;
}
