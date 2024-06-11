package org.mounanga.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mounanga.userservice.dto.AuthenticationRequest;
import org.mounanga.userservice.dto.AuthenticationResponse;
import org.mounanga.userservice.entity.Role;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.exception.UserNotAuthenticatedException;
import org.mounanga.userservice.repository.UserRepository;
import org.mounanga.userservice.util.ApplicationProperties;
import org.mounanga.userservice.web.restclient.NotificationRestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationProperties properties;

    @Mock
    private NotificationRestClient notificationRestClient;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(
                authenticationManager, userRepository, properties, notificationRestClient
        );
    }

    @Test
    void testAuthenticate_success() {
        AuthenticationRequest request = new AuthenticationRequest("john.doe@mail.com", "password");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        User user = new User();
        user.setUsername("user");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@mail.com");
        user.setRoles(Collections.singletonList(new Role(1L,"ROLE_USER")));

        when(userRepository.findByUsernameOrEmail(anyString())).thenReturn(Optional.of(user));
        when(properties.getJwtSecret()).thenReturn("secret");
        when(properties.getJwtExpiration()).thenReturn(3600000L); // 1 hour

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertNotNull(response.jwt());
        verify(notificationRestClient, times(1)).sendNotification(any());
    }

    @Test
    void testAuthenticate_failure() {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Authentication failed") {});

        assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(request));
    }


}