package org.mounanga.accountservice.commands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotBlank(message="field 'accountIdFrom' is mandatory: it can not be blank")
        String accountIdFrom,

        @NotBlank(message="field 'accountIdTo' is mandatory: it can not be blank")
        String accountIdTo,

        @NotNull(message ="field 'accountId' is mandatory: it can not be null")
        @Positive(message = "amount must be positive")
        BigDecimal amount,

        @NotBlank(message="field 'description' is mandatory: it can not be blank")
        String description) {
}

