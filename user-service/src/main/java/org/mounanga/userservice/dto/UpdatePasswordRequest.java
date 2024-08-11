package org.mounanga.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank(message = "field 'email' is mandatory: it cannot be blank")
        @Email(message = "e-mail is not well formed")
        String email,

        @NotBlank(message = "field 'code' is mandatory: it cannot be blank")
        @Size(min = 8, message = "the minimum code size is 6 numeric characters.")
        String code,

        @NotBlank(message = "field 'password' is mandatory: it cannot be blank")
        @Size(min = 8, message = "the minimum password size is 8 alpha-numeric characters.")
        String password) {
}
