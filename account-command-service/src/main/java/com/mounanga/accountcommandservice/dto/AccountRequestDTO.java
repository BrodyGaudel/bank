package com.mounanga.accountcommandservice.dto;

import com.mounanga.accountcommandservice.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountRequestDTO {

    @NotNull(message = "currency is mandatory : it can not be null")
    private Currency currency;

    @NotNull(message = "customer's id is mandatory : it can not be null")
    private UUID customerId;
}
