package org.mounanga.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "field 'username' is mandatory: it cannot be blank")
        String username,
        @NotBlank(message = "field 'password' is mandatory: it cannot be blank")
        String password) {
}
