package org.mounanga.userservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.userservice.dto.AuthenticationRequest;
import org.mounanga.userservice.dto.AuthenticationResponse;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.exception.UserNotAuthenticatedException;
import org.mounanga.userservice.exception.UserNotFoundException;
import org.mounanga.userservice.repository.UserRepository;
import org.mounanga.userservice.util.ApplicationProperties;
import org.mounanga.userservice.web.restclient.Notification;
import org.mounanga.userservice.web.restclient.NotificationRestClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ApplicationProperties properties;
    private final NotificationRestClient notificationRestClient;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, ApplicationProperties properties, NotificationRestClient notificationRestClient) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.properties = properties;
        this.notificationRestClient = notificationRestClient;
    }

    @Override
    public AuthenticationResponse authenticate(@NotNull AuthenticationRequest request) {
        log.info("In authenticate()");
        Authentication authenticationRequest =  new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        if (authenticationResponse.isAuthenticated()) {
            log.info("Authentication successful for user: {}", request.usernameOrEmail());
            return new AuthenticationResponse(generateToken(request.usernameOrEmail()));
        }else{
            log.error("Authentication failed for user: {}", request.usernameOrEmail());
            throw new UserNotAuthenticatedException("User not authenticated");
        }
    }


    private String generateToken(@NotNull String usernameOrEmail) {
        User user = getUserByUsernameOrEmail(usernameOrEmail);
        List<String> roles = getListOfNamesOfRoles(user.getRoles());
        sendNotification(user);
        return createJwt(user, roles);
    }

    private User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> {
                    log.error("User not found with username or email: {}", usernameOrEmail);
                    return new UserNotFoundException("User not found");
                });
    }

    private List<String> getListOfNamesOfRoles(@NotNull List<Role> roles) {
        return roles.stream().map(Role::getName).toList();
    }

    private String createJwt(@NotNull User user, @NotNull List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(properties.getJwtSecret());
        Date expiration = new Date(System.currentTimeMillis() + properties.getJwtExpiration());
        return JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[0]))
                .withClaim("fullName", user.getFullName())
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    private void sendNotification(@NotNull User user) {
        try{
            Notification notification = new Notification(LocalDateTime.now(), "where", user.getFullName(), user.getEmail());
            notificationRestClient.sendNotification(notification);
            log.info("Authentication's notification sent to user: {}", user.getEmail());
        }catch (Exception e){
            log.error("Failed to send notification to user: {}", user.getEmail(), e);
        }
    }


}
