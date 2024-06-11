package org.mounanga.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "field 'usernameOrEmail' is mandatory : it can not be blank") String usernameOrEmail,
        @NotBlank(message = "field 'password' is mandatory : it can not be blank") String password) {
}
