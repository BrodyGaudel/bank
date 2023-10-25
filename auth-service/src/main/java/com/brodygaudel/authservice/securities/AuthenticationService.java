package com.brodygaudel.authservice.securities;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.brodygaudel.authservice.dtos.AuthenticationRequest;
import com.brodygaudel.authservice.dtos.AuthenticationResponse;
import com.brodygaudel.authservice.entities.User;
import com.brodygaudel.authservice.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request){
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
                .withExpiresAt(new Date(System.currentTimeMillis()+SecParams.EXP_TIME))
                .sign(Algorithm.HMAC256(SecParams.SECRET));

        return new AuthenticationResponse(jwt, user.getUsername());
    }
}
