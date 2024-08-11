package org.mounanga.userservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mounanga.userservice.dto.NotificationRequest;
import org.mounanga.userservice.dto.UpdatePasswordRequest;
import org.mounanga.userservice.entity.User;
import org.mounanga.userservice.entity.Verification;
import org.mounanga.userservice.exception.UserNotFoundException;
import org.mounanga.userservice.exception.VerificationNotFoundException;
import org.mounanga.userservice.repository.UserRepository;
import org.mounanga.userservice.repository.VerificationRepository;
import org.mounanga.userservice.service.PasswordService;
import org.mounanga.userservice.util.CodeGenerator;
import org.mounanga.userservice.util.NotificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PasswordServiceImpl implements PasswordService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public PasswordServiceImpl(VerificationRepository verificationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, NotificationService notificationService) {
        this.verificationRepository = verificationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }


    @Transactional
    @Override
    public void requestNewVerificationCode(String email) {
        log.info("Verifying new verification code for user {}", email);
        User user = findUserByEmail(email);
        verificationRepository.deleteByEmail(user.getEmail());
        Verification verification = new Verification();
        verification.setCode(CodeGenerator.generateVerificationCode());
        verification.setEmail(user.getEmail());
        verification.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        Verification savedVerification = verificationRepository.save(verification);
        sendVerificationEmail(savedVerification.getEmail(), verification.getCode());
        log.info("New verification code has been sent");
    }

    @Transactional
    @Override
    public void updatePassword(@NotNull UpdatePasswordRequest request) {
        log.info("In updatePassword()");
        Verification verification = verificationRepository.findByEmail(request.email())
                .orElseThrow(() -> new VerificationNotFoundException(String.format("Verification with email %s not found", request.email())));
        if(!verification.getCode().equals(request.code())){
            throw new IllegalArgumentException("The verification code provided does not match our records.");
        }
        if(verification.isExpired()){
            throw new IllegalArgumentException("the verification code has expired. ask for another one");
        }
        User user = findUserByEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPasswordNeedToBeModified(false);
        User updatedUser = userRepository.save(user);
        log.info("user with id {} has been updated (password)", updatedUser.getId());
        verificationRepository.deleteByEmail(request.email());
        sendNotificationEmail(updatedUser.getEmail());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow( () -> new UserNotFoundException(String.format("User with email %s not found", email)));
    }



    private void sendVerificationEmail(String email, String verificationCode) {
        final String body = String.format(
                "Hello, this is your verification code: %s. This code expires in 10 minutes. "
                        + "If you have not requested this code, please inform the administrator.",
                verificationCode
        );
        final String subject = "Verification Code";
        notificationService.send(new NotificationRequest(email,subject, body));
    }

    private void sendNotificationEmail(String email) {
        final String body = "Hello, your password has just been changed. If you are not the author, please contact the administrator urgently.";
        final String subject = "Password changed";
        notificationService.send(new NotificationRequest(email,subject, body));
    }
}
