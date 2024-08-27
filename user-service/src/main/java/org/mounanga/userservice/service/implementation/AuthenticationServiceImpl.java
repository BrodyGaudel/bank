package org.mounanga.userservice.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.userservice.dto.LoginRequest;
import org.mounanga.userservice.dto.LoginResponse;
import org.mounanga.userservice.dto.NotificationRequest;
import org.mounanga.userservice.entity.Profile;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.exception.UserNotAuthenticatedException;
import org.mounanga.userservice.exception.UserNotEnabledException;
import org.mounanga.userservice.exception.UserNotFoundException;
import org.mounanga.userservice.repository.UserRepository;
import org.mounanga.userservice.service.AuthenticationService;
import org.mounanga.userservice.util.ApplicationProperties;
import org.mounanga.userservice.util.NotificationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ApplicationProperties properties;
    private final NotificationService notificationService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, ApplicationProperties properties, NotificationService notificationService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.properties = properties;
        this.notificationService = notificationService;
    }

    @Override
    public LoginResponse authenticate(@NotNull LoginRequest request) {
        log.info("In authenticate()");
        Authentication authenticationRequest =  new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        if (authenticationResponse.isAuthenticated()) {
            User user = getUserByUsername(request.username());
            if(!user.isEnabled()){
                throw new UserNotEnabledException(String.format("User %s is not enabled", request.username()));
            }
            log.info("Authentication successful");
            sendNotification(user.getEmail(), getFullName(user.getProfile()));
            final String jwt = generateToken(user);
            return new LoginResponse(jwt, user.isPasswordNeedToBeModified());
        }else{
            log.error("Authentication failed for user: {}", request.username());
            throw new UserNotAuthenticatedException("User not authenticated");
        }
    }

    private String generateToken(@NotNull User user) {
        List<String> roles = getListOfNamesOfRoles(user.getRoles());
        return createJwt(user, roles);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User not found with username or email: {}", username);
            return new UserNotFoundException("User not found");
        });
    }

    private List<String> getListOfNamesOfRoles(List<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        return roles.stream().map(Role::getName).toList();
    }


    private void sendNotification(String to, String fullName) {
        final String body = "Hello " + fullName + "! . You have just logged into your workspace. If you are not the source of this manoeuvre: please change your password immediately and contact an administrator.";
        final String subject = "Authentication Notification";
        notificationService.send(new NotificationRequest(to, subject, body));
    }

    private String createJwt(@NotNull User user, @NotNull List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(properties.getJwtSecret());
        Date expiration = new Date(System.currentTimeMillis() + properties.getJwtExpiration());
        return JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withClaim("fullName", getFullName(user.getProfile()))
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    private String getFullName(Profile profile) {
        if(profile == null){
            return "UNKNOWN";
        }
        return profile.getFullName();
    }
}
