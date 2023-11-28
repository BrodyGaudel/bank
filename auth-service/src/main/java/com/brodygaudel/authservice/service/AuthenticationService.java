package com.brodygaudel.authservice.service;

import com.brodygaudel.authservice.dto.AuthenticationRequest;
import com.brodygaudel.authservice.dto.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
