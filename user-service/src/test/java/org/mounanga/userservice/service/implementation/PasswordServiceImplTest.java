package org.mounanga.userservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.userservice.dto.NotificationRequest;
import org.mounanga.userservice.dto.UpdatePasswordRequest;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.entity.Verification;
import org.mounanga.userservice.exception.UserNotFoundException;
import org.mounanga.userservice.repository.UserRepository;
import org.mounanga.userservice.repository.VerificationRepository;
import org.mounanga.userservice.util.NotificationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PasswordServiceImplTest {

    @Mock
    private VerificationRepository verificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PasswordServiceImpl passwordService;

    private User user;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordServiceImpl(verificationRepository, userRepository, passwordEncoder, notificationService);
        user = User.builder()
                .email("test@example.com")
                .username("testuser")
                .password("encodedPassword")
                .build();
    }

    @Test
    void requestNewVerificationCodeShouldSendVerificationEmail() {
        // Arrange
        Verification verification = Verification.builder()
                .email(user.getEmail())
                .code("123456")
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(verificationRepository.save(any(Verification.class))).thenReturn(verification);

        // Act
        passwordService.requestNewVerificationCode(user.getEmail());

        // Assert
        verify(verificationRepository).deleteByEmail(user.getEmail());
        verify(verificationRepository).save(any(Verification.class));
        verify(notificationService).send(any(NotificationRequest.class));
    }


    @Test
    void requestNewVerificationCodeShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> passwordService.requestNewVerificationCode(anyString()));
        verify(verificationRepository, never()).deleteByEmail(anyString());
        verify(verificationRepository, never()).save(any(Verification.class));
        verify(notificationService, never()).send(any(NotificationRequest.class));
    }

    @Test
    void updatePasswordShouldUpdateUserPasswordWhenVerificationIsValid() {
        // Arrange
        String email = "test@example.com";
        String code = "123456";
        String newPassword = "newSecurePassword";

        Verification verification = Verification.builder()
                .email(email)
                .code(code)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        UpdatePasswordRequest request = new UpdatePasswordRequest(email, code, newPassword);

        // Mocking
        when(verificationRepository.findByEmail(email)).thenReturn(Optional.of(verification));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        passwordService.updatePassword(request);

        // Assert
        verify(verificationRepository).findByEmail(email);
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
        verify(verificationRepository).deleteByEmail(email);
        verify(notificationService).send(any(NotificationRequest.class));

        assertEquals(passwordEncoder.encode(newPassword), user.getPassword());
        assertFalse(user.isPasswordNeedToBeModified());
    }


    @Test
    void updatePassword_shouldThrowException_whenVerificationCodeIsInvalid() {
        // Arrange
        Verification verification = Verification.builder()
                .email(user.getEmail())
                .code("123456")
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();

        UpdatePasswordRequest request = new UpdatePasswordRequest(user.getEmail(), "WrongCode", "newPassword123");

        when(verificationRepository.findByEmail(request.email())).thenReturn(Optional.of(verification));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> passwordService.updatePassword(request));
        verify(userRepository, never()).save(any(User.class));
        verify(verificationRepository, never()).deleteByEmail(anyString());
        verify(notificationService, never()).send(any(NotificationRequest.class));
    }

    @Test
    void updatePasswordShouldThrowExceptionWhenVerificationCodeIsExpired() {
        // Arrange
        Verification verification = Verification.builder()
                .email(user.getEmail())
                .code("123456")
                .expiryDate(LocalDateTime.now().minusMinutes(1))
                .build();

        UpdatePasswordRequest request = new UpdatePasswordRequest(user.getEmail(), "123456", "newPassword123");


        when(verificationRepository.findByEmail(request.email())).thenReturn(Optional.of(verification));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> passwordService.updatePassword(request));
        verify(userRepository, never()).save(any(User.class));
        verify(verificationRepository, never()).deleteByEmail(anyString());
        verify(notificationService, never()).send(any(NotificationRequest.class));
    }
}