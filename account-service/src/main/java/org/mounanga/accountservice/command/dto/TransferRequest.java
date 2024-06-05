package org.mounanga.accountservice.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "field 'accountIdFrom' is mandatory : it can not be blank.")
        @Size(min = 12, max = 12, message = "field 'accountIdFrom' size's must be equals to 12")
        String accountIdFrom,

        @NotBlank(message = "field 'accountIdTo' is mandatory : it can not be blank.")
        @Size(min = 12, max = 12, message = "field 'accountIdTo' size's must be equals to 12")
        String accountIdTo,

        @NotNull(message = "field 'amount' is mandatory : it can not be null.")
        BigDecimal amount,
        String description) {
}
