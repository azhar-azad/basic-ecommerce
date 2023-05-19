package com.azad.basicecommerce.security;

import com.azad.basicecommerce.common.ApiUtils;
import com.azad.basicecommerce.model.auth.*;
import com.azad.basicecommerce.repository.AppUserRepository;
import com.azad.basicecommerce.repository.RoleRepository;
import com.azad.basicecommerce.security.jwt.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    public AppUserDto registerUser(AppUserDto dto) {
        if (dto.getRoleName() == null || dto.getRoleName().equalsIgnoreCase(""))
            dto.setRoleName("USER");

        String roleName = dto.getRoleName();

        if (roleName.equalsIgnoreCase("ADMIN")) {
            if (!loggedInUserIsAdmin())
                throw new RuntimeException("Only admins can create a new Admin user");
        }

        String encodedPass = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPass);
        dto.setUid(apiUtils.getHash("user", dto.getEmail() + dto.getUsername()
                + dto.getFirstName() + dto.getLastName()));

        RoleEntity role = roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new RuntimeException("Role not found with name: " + roleName));

        AppUserEntity user = modelMapper.map(dto, AppUserEntity.class);
        user.setRole(role);
        user.setEnabled(true);
        user.setExpired(false);
        user.setLocked(false);
        user.setCreatedAt(LocalDateTime.now());

        return modelMapper.map(appUserRepository.save(user), AppUserDto.class);
    }

    public String authenticateAndGetUsernameOrEmail(LoginRequest loginRequest) throws AuthenticationException {
        String usernameOrEmail = getUsernameOrEmail(modelMapper.map(loginRequest, AppUserDto.class));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(usernameOrEmail, loginRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);

        return usernameOrEmail;
    }

    public AppUserDto updateUser(AppUserDto updatedDto) {
        AppUserEntity loggedInUser = getLoggedInUser();

        if (updatedDto.getFirstName() != null)
            loggedInUser.setFirstName(updatedDto.getFirstName());
        if (updatedDto.getLastName() != null)
            loggedInUser.setLastName(updatedDto.getLastName());
        loggedInUser.setUpdatedAt(LocalDateTime.now());

        AppUserEntity updatedUser = appUserRepository.save(loggedInUser);
        return modelMapper.map(updatedUser, AppUserDto.class);
    }

    public void deleteUser() {
        AppUserEntity loggedInUser = getLoggedInUser();
        appUserRepository.delete(loggedInUser);
    }

    public void resetPassword(String updatedPassword) {
        AppUserEntity loggedInUser = getLoggedInUser();

        String encodedNewPass = passwordEncoder.encode(updatedPassword);
        loggedInUser.setPassword(encodedNewPass);

        appUserRepository.save(loggedInUser);
    }

    public <U extends AppUser> String getUsernameOrEmail(U u) {
        String usernameOrEmail = securityUtils.isUsernameBasedAuth() ? u.getUsername() : securityUtils.isEmailBasedAuth() ? u.getEmail() : null;
        if (usernameOrEmail == null)
            throw new RuntimeException("Unknown authentication base configured: " + securityUtils.getAuthBase());
        return usernameOrEmail;
    }

    public AppUserEntity getLoggedInUser() {
        String usernameOrEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return securityUtils.getUserByAuthBase(usernameOrEmail);
    }

    public boolean loggedInUserIsAdmin() {
        return getLoggedInUser().getRole().getRoleName().equalsIgnoreCase("ADMIN");
    }

    public ResponseEntity<Map<String, String>> generateTokenAndSend(String authenticatedUserId, HttpStatus statusToSend) {
        String token = jwtUtils.generateJwtToken(authenticatedUserId);
        return new ResponseEntity<>(Collections.singletonMap("TOKEN", token), statusToSend);
    }
}
