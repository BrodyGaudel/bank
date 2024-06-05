package org.mounanga.accountservice.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.mounanga.accountservice.common.enums.Status;

public record UpdateAccountStatusRequest(
        @NotBlank(message = "field 'accountId' is mandatory : it can not be blank.")
        @Size(min = 12, max = 12, message = "field 'accountId' size's must be equals to 12")
        String accountId,
        @NotNull(message = "field 'status' is mandatory : it can not be null.")
        Status status) {
}
