package org.mounanga.userservice.service;

import org.mounanga.userservice.dto.LoginRequest;
import org.mounanga.userservice.dto.LoginResponse;

public interface AuthenticationService {
    LoginResponse authenticate(LoginRequest request);
}
