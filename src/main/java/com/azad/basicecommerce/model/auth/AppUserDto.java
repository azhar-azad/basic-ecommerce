package com.azad.basicecommerce.model.auth;

import com.azad.basicecommerce.model.address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AppUserDto extends AppUser {

    private Long id;
    private Role role;
    private String roleName;
    private Address address;
    private List<Address> addresses;
}
