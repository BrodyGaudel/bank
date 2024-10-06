package org.mounanga.authenticationservice.web;

import org.mounanga.authenticationservice.dto.ChangePasswordRequestDTO;
import org.mounanga.authenticationservice.service.PasswordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passwords")
public class PasswordRestController {

    private final PasswordService passwordService;

    public PasswordRestController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/ask/{email}")
    public void requestCodeToResetPassword(@PathVariable String email){
        passwordService.requestCodeToResetPassword(email);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestBody ChangePasswordRequestDTO dto){
        passwordService.resetPassword(dto);
    }
}
