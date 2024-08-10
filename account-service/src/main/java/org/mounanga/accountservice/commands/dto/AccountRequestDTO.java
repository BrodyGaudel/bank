package org.mounanga.accountservice.commands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.mounanga.accountservice.common.enums.Currency;

public record AccountRequestDTO(
        @NotBlank(message="field 'customerId' is mandatory: it can not be blank")
        String customerId,

        @NotNull(message="field 'currency' is mandatory: it can not be null")
        Currency currency) {
}
