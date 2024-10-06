package org.mounanga.authenticationservice.service;

import org.mounanga.authenticationservice.dto.LoginRequestDTO;
import org.mounanga.authenticationservice.dto.LoginResponseDTO;

public interface AuthenticationService {

    LoginResponseDTO authenticate(LoginRequestDTO dto);
}
