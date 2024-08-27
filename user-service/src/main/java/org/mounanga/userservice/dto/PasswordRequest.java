package org.mounanga.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordRequest(
        @NotBlank(message = "password is mandatory: it can not be blank")
        @Size(min = 8, message = "password's size must be upper 8 characters")
        String password,

        @NotBlank(message = "confirm password is mandatory: it can not be blank")
        @Size(min = 8, message = "confirm password's size must be upper 8 characters")
        String confirmPassword) {
}
