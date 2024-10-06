package org.mounanga.authenticationservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDTO(
        @NotBlank(message = "field 'name' is mandatory : it can not be blank")
        String name,

        @NotBlank(message = "field 'description' is mandatory : it can not be blank")
        String description) {
}
