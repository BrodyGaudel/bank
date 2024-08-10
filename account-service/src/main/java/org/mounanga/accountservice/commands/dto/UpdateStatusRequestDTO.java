package org.mounanga.accountservice.commands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.mounanga.accountservice.common.enums.AccountStatus;

public record UpdateStatusRequestDTO(
        @NotBlank(message="field 'accountId' is mandatory: it can not be blank")
        String accountId,
        @NotNull(message = "field 'status' is mandatory: it can not be null")
        AccountStatus status) {
}
