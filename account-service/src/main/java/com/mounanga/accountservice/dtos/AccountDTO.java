package com.mounanga.accountservice.dtos;

import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.enums.Currency;

import java.math.BigDecimal;
import java.util.Date;

public record AccountDTO(
        String id,
        String customerId,
        BigDecimal balance,
        Currency currency,
        AccountStatus status,
        Date creation,
        Date lastUpdate
) {
}
