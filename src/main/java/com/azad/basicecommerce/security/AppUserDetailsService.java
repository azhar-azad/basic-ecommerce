package com.azad.basicecommerce.security;

import com.azad.basicecommerce.model.auth.AppUserEntity;
import com.azad.basicecommerce.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AppUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        AppUserEntity user;
        try {
            user = securityUtils.getUserByAuthBase(usernameOrEmail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found with username/email " + usernameOrEmail);
        }

        return new AppUserDetails(user);
    }
}
