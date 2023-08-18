package com.mounanga.operationservice.dtos;

import com.mounanga.operationservice.enums.Currency;
import com.mounanga.operationservice.enums.Status;

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
