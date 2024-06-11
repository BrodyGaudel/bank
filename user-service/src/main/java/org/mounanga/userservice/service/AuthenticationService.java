package org.mounanga.userservice.service;

import org.mounanga.userservice.dto.AuthenticationRequest;
import org.mounanga.userservice.dto.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
