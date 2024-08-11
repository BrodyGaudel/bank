package org.mounanga.userservice.web;

import jakarta.validation.Valid;
import org.mounanga.userservice.dto.LoginRequest;
import org.mounanga.userservice.dto.LoginResponse;
import org.mounanga.userservice.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public LoginResponse authenticate(@RequestBody @Valid LoginRequest request){
        return authenticationService.authenticate(request);
    }
}
