package org.mounanga.userservice.web;

import jakarta.validation.Valid;
import org.mounanga.userservice.dto.UpdatePasswordRequest;
import org.mounanga.userservice.service.PasswordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pwd")
public class PasswordRestController {

    private final PasswordService passwordService;

    public PasswordRestController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/ask")
    public void requestNewVerificationCode(@RequestParam(name="email") String email){
        passwordService.requestNewVerificationCode(email);
    }

    @PostMapping("/update")
    public void updatePassword(@RequestBody @Valid UpdatePasswordRequest request){
        passwordService.updatePassword(request);
    }
}
