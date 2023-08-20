package com.mounanga.accountservice.dtos;

import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.OperationType;

import java.math.BigDecimal;
import java.util.Date;

public record OperationDTO(
        String id,
        Date date,
        OperationType type,
        BigDecimal amount,
        Currency currency,
        String description,
        String accountId) {
}
