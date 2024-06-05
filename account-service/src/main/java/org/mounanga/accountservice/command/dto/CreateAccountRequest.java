package org.mounanga.accountservice.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.mounanga.accountservice.common.enums.Currency;

public record CreateAccountRequest(
        @NotNull(message = "field 'Currency' is mandatory: it can not be null")
        Currency currency,
        @NotBlank(message = "field 'customer's id' is mandatory: it can not be blank")
        String customerId) {
}
