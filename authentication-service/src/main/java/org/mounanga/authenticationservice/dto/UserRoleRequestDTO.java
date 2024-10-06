package org.mounanga.authenticationservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRoleRequestDTO(
        @NotBlank(message = "field 'username' is mandatory : it can not be blank")
        String username,

        @NotBlank(message = "field 'roleName' is mandatory : it can not be blank")
        String roleName) {
}
