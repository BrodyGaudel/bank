package org.mounanga.accountservice.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DebitAccountRequest(
        @NotBlank(message = "field 'accountId' is mandatory : it can not be blank.")
        @Size(min = 12, max = 12, message = "field 'accountId' size's must be equals to 12")
        String accountId,

        @NotBlank(message = "field 'accountId' is mandatory : it can not be blank.")
        String description,

        @NotNull(message = "field 'amount' is mandatory : it can not be null.")
        BigDecimal amount) {
}
