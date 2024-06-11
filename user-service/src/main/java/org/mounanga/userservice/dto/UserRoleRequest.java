package org.mounanga.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRoleRequest(@NotBlank(message = "field 'roleName' is mandatory : it can not be blank.") String roleName,
                              @NotBlank(message = "field 'username' is mandatory : it can not be blank.") String username) {
}
