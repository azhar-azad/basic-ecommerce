package com.azad.basicecommerce.api.resource;

import com.azad.basicecommerce.api.assembler.AppUserResponseModelAssembler;
import com.azad.basicecommerce.model.address.Address;
import com.azad.basicecommerce.model.auth.AppUserDto;
import com.azad.basicecommerce.model.auth.AppUserResponse;
import com.azad.basicecommerce.model.auth.LoginRequest;
import com.azad.basicecommerce.model.auth.RegistrationRequest;
import com.azad.basicecommerce.security.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthRestResource {

    @Autowired
    private ModelMapper modelMapper;

    private final AuthService authService;
    private final AppUserResponseModelAssembler assembler;

    @Autowired
    public AuthRestResource(AuthService authService, AppUserResponseModelAssembler assembler) {
        this.authService = authService;
        this.assembler = assembler;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Map<String, String>> handleUserRegistration(@Valid @RequestBody RegistrationRequest request) {

        AppUserDto dto = modelMapper.map(request, AppUserDto.class);
        AppUserDto registeredUser = authService.registerUser(dto);

        return authService.generateTokenAndSend(authService.getUsernameOrEmail(registeredUser), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, String>> handleUserLogin(@Valid @RequestBody LoginRequest request) {

        if (request.getEmail() == null && request.getUsername() == null)
            throw new RuntimeException("Invalid login request. No username or email to authenticate.");

        try {
            String authenticatedUsernameOrEmail = authService.authenticateAndGetUsernameOrEmail(request);
            return authService.generateTokenAndSend(authenticatedUsernameOrEmail, HttpStatus.OK);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(Collections.singletonMap("Authentication Error", ex.getLocalizedMessage()),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<EntityModel<AppUserResponse>> getUser() {
        AppUserResponse response = modelMapper.map(authService.getLoggedInUser(), AppUserResponse.class);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.OK);
    }

    @PutMapping(path = "/me")
    public ResponseEntity<EntityModel<AppUserResponse>> updateUser(@RequestBody AppUserDto updatedDto) {

        AppUserDto savedDto = authService.updateUser(updatedDto);
        return new ResponseEntity<>(assembler.toModel(modelMapper.map(savedDto, AppUserResponse.class)), HttpStatus.OK);
    }

    @DeleteMapping(path = "/me")
    public ResponseEntity<?> handleUserUnRegistration() {
        authService.deleteUser();
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/me/resetPassword")
    public ResponseEntity<?> handleResetPassword(@RequestBody RegistrationRequest request) {
        authService.resetPassword(request.getPassword());
        return ResponseEntity.accepted().build();
    }

    @PostMapping(path = "/me/address")
    public ResponseEntity<EntityModel<AppUserResponse>> createAddress(@Valid @RequestBody Address address) {

        AppUserDto dto = new AppUserDto();
        dto.setAddress(address);

        AppUserDto updatedDto = authService.addAddress(dto);

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(updatedDto, AppUserResponse.class)),
                HttpStatus.CREATED);
    }

    @PutMapping(path = "/me/address")
    public ResponseEntity<EntityModel<AppUserResponse>> updateAddress(@Valid @RequestBody Address updatedAddress) {

        AppUserDto dto = new AppUserDto();
        dto.setAddress(updatedAddress);

        AppUserDto updatedDto = authService.updateAddress(dto);

        return new ResponseEntity<>(assembler.toModel(modelMapper.map(updatedDto, AppUserResponse.class)),
                HttpStatus.OK);
    }
}
