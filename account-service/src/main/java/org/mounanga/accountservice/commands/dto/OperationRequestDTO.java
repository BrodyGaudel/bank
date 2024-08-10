package org.mounanga.accountservice.commands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.math.BigDecimal;

public record OperationRequestDTO(
        @NotBlank(message="field 'accountId' is mandatory: it can not be blank")
        String accountId,

        @NotNull(message ="field 'accountId' is mandatory: it can not be null")
        @Positive(message = "amount must be positive")
        BigDecimal amount,

        @NotBlank(message="field 'description' is mandatory: it can not be blank")
        String description) {
}
