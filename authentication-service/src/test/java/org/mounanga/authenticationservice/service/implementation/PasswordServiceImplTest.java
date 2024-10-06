package org.mounanga.authenticationservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.authenticationservice.dto.ChangePasswordRequestDTO;
import org.mounanga.authenticationservice.entity.User;
import org.mounanga.authenticationservice.entity.Verification;
import org.mounanga.authenticationservice.exception.TooManyRequestsException;
import org.mounanga.authenticationservice.exception.VerificationCodeExpiredException;
import org.mounanga.authenticationservice.repository.UserRepository;
import org.mounanga.authenticationservice.repository.VerificationRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    @BeforeEach
    void setUp() {
        passwordService = new PasswordServiceImpl(verificationRepository, userRepository, passwordEncoder, notificationService);
    }

    @Test
    void testRequestCodeToResetPasswordSuccess() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        // Mocking the findUserByEmail method
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // No existing verification
        when(verificationRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Mocking the creation and saving of a new verification
        Verification verification = new Verification();
        verification.setEmail(email);
        verification.setCode("123456");
        verification.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        when(verificationRepository.save(any(Verification.class))).thenReturn(verification);

        // Execute the method
        passwordService.requestCodeToResetPassword(email);

        // Verify interactions
        verify(verificationRepository).findByEmail(email);
        verify(verificationRepository).save(any(Verification.class));
        verify(notificationService).send(eq(email), anyString(), contains("123456"));
    }

    @Test
    void testRequestCodeToResetPasswordExistingVerificationNotExpired() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        // Mocking the findUserByEmail method
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Mocking an existing non-expired verification
        Verification existingVerification = new Verification();
        existingVerification.setEmail(email);
        existingVerification.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        when(verificationRepository.findByEmail(email)).thenReturn(Optional.of(existingVerification));

        // Assert that the TooManyRequestsException is thrown
        TooManyRequestsException exception = assertThrows(TooManyRequestsException.class, () ->
                passwordService.requestCodeToResetPassword(email)
        );

        assertEquals("You must wait for 10 minutes before requesting a new code", exception.getMessage());

        // Verify that no new verification is created or sent
        verify(verificationRepository, never()).save(any(Verification.class));
        verify(notificationService, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void testResetPasswordSuccess() {
        String email = "test@example.com";
        String code = "123456";
        String newPassword = "newPass123";
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(email, code, newPassword, newPassword);

        // Mocking verification lookup
        Verification verification = new Verification();
        verification.setEmail(email);
        verification.setCode(code);
        verification.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        when(verificationRepository.findByEmailAndCode(anyString(), anyString())).thenReturn(Optional.of(verification));

        // Mocking user lookup
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Mock password encoding
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // Execute the resetPassword method
        passwordService.resetPassword(dto);

        // Verify user password is updated and saved
        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());

        // Verify that the verification is deleted
        verify(verificationRepository).delete(verification);
    }

    @Test
    void testResetPasswordCodeExpired() {
        String email = "test@example.com";
        String code = "123456";
        String newPassword = "newPass123";
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(email, code, newPassword, newPassword);

        // Mocking expired verification
        Verification verification = new Verification();
        verification.setEmail(email);
        verification.setCode(code);
        verification.setExpiryDate(LocalDateTime.now().minusMinutes(1)); // expired
        when(verificationRepository.findByEmailAndCode(anyString(), anyString())).thenReturn(Optional.of(verification));

        // Assert that the VerificationCodeExpiredException is thrown
        VerificationCodeExpiredException exception = assertThrows(VerificationCodeExpiredException.class, () ->
                passwordService.resetPassword(dto)
        );

        assertEquals("code is expired : ask for another code", exception.getMessage());

        // Verify that no user password is updated
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testResetPasswordVerificationCodeExpired() {
        String email = "test@example.com";
        String code = "123456";
        String password = "password1";
        String confirmPassword = "password2"; // Mismatching passwords
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(email, code, password, confirmPassword);

        Verification verification = new Verification();
        verification.setEmail(email);
        verification.setCode(code);
        verification.setExpiryDate(LocalDateTime.now().minusMinutes(1)); // expired
        when(verificationRepository.findByEmailAndCode(anyString(), anyString())).thenReturn(Optional.of(verification));

        assertThrows(VerificationCodeExpiredException.class, () -> passwordService.resetPassword(dto));
    }
}