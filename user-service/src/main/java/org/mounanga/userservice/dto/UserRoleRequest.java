package org.mounanga.userservice.dto;

import jakarta.validation.constraints.NotNull;

public record UserRoleRequest(
        @NotNull(message = "field 'userId' is mandatory: it can not be null")
        Long userId,
        @NotNull(message = "field 'roleId' is mandatory: it can not be null")
        Long roleId) {
}
