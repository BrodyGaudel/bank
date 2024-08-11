package org.mounanga.userservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.mounanga.userservice.util.ApplicationProperties;
import org.mounanga.userservice.util.NotificationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationProperties properties;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    List<Role> roles;

    @BeforeEach
    void setUp() {
        roles = new ArrayList<>();
        roles.add(Role.builder().name("ROLE_USER").description("Description").build());
        authenticationService = new AuthenticationServiceImpl(authenticationManager, userRepository, properties, notificationService);
    }

    @Test
    void authenticateShouldReturnJwtTokenWhenAuthenticationIsSuccessful() {
        // Arrange
        String username = "testuser";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticationResponse = mock(Authentication.class);


        User user = User.builder()
                .username(username)
                .password(password)
                .enabled(true)
                .roles(roles)
                .profile(Profile.builder().firstname("Full").lastname("Name").build())
                .build();

        when(authenticationResponse.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(properties.getJwtSecret()).thenReturn("secret");
        when(properties.getJwtExpiration()).thenReturn(3600000L);

        // Act
        LoginResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.passwordNeedToBeModified());
        verify(notificationService).send(any(NotificationRequest.class));
    }

    @Test
    void authenticateShouldThrowUserNotEnabledExceptionWhenUserIsNotEnabled() {
        // Arrange
        String username = "testuser";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticationResponse = mock(Authentication.class);

        User user = User.builder()
                .username(username)
                .password(password)
                .enabled(false) // User is not enabled
                .roles(roles)
                .build();

        when(authenticationResponse.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserNotEnabledException.class, () -> authenticationService.authenticate(request));
        verify(notificationService, never()).send(any(NotificationRequest.class));
    }

    @Test
    void authenticateShouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        String username = "unknownuser";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticationResponse = mock(Authentication.class);

        when(authenticationResponse.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate(request));
        verify(notificationService, never()).send(any(NotificationRequest.class));
    }

    @Test
    void authenticateShouldThrowUserNotAuthenticatedExceptionWhenAuthenticationFails() {
        // Arrange
        String username = "testuser";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticationResponse = mock(Authentication.class);

        when(authenticationResponse.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        // Act & Assert
        assertThrows(UserNotAuthenticatedException.class, () -> authenticationService.authenticate(request));
        verify(notificationService, never()).send(any(NotificationRequest.class));
    }
}