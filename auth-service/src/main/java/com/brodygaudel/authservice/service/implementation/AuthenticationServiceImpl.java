package com.brodygaudel.authservice.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brodygaudel.authservice.dto.AuthenticationRequest;
import com.brodygaudel.authservice.dto.AuthenticationResponse;
import com.brodygaudel.authservice.entity.User;
import com.brodygaudel.authservice.repository.UserRepository;
import com.brodygaudel.authservice.security.SecParams;
import com.brodygaudel.authservice.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecParams secParams;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, SecParams secParams) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.secParams = secParams;
    }

    @Override
    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request) {
        log.info("In authenticate()");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username());
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(
                role -> roles.add(role.getName())
        );

        String jwt = JWT.create().withSubject(user.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis()+secParams.getExpiredTime()))
                .sign(Algorithm.HMAC256(secParams.getSecret()));
        log.info("user authenticated");
        return new AuthenticationResponse(jwt, user.getUsername());
    }
}
