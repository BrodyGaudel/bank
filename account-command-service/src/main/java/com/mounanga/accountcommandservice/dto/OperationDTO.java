package com.mounanga.accountcommandservice.dto;

import com.mounanga.accountcommandservice.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OperationDTO {

    @NotBlank(message = "rib is mandatory: it can not be null or blank")
    private String rib;

    @NotNull(message = "amount is mandatory: it can not be null")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "currency is mandatory: it can not be null")
    private Currency currency;
}
