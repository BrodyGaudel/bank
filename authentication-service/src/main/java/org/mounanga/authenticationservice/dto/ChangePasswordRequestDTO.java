package org.mounanga.authenticationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.mounanga.authenticationservice.util.validation.Password;

public record ChangePasswordRequestDTO(
        @NotBlank(message = "field 'email' is mandatory : it can not be blank")
        @Email(message = "field 'email' must be well formated as 'example@mail.com'")
        String email,

        @NotBlank(message = "field 'code' is mandatory : it can not be blank")
        @Size(message = "size of field 'code' must be equals to 6", min = 6, max = 6)
        String code,

        @Password(message = "field 'password' is not well formated : it must have at least two uppercase letters, two lowercase letters, two numbers and one special character.")
        String password,

        @Password(message = "field 'confirmPassword' is not well formated : it must have at least two uppercase letters, two lowercase letters, two numbers and one special character.")
        String confirmPassword) {
}
