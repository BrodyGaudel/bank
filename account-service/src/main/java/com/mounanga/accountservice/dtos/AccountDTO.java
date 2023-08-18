package com.mounanga.accountservice.dtos;

import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.Status;

import java.math.BigDecimal;
import java.util.Date;

public record AccountDTO(
        String id,
        Currency currency,
        BigDecimal balance,
        Status status,
        String customerId,
        Date creation,
        Date lastUpdate) {
}
