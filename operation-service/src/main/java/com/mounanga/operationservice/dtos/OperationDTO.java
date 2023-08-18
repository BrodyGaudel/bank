package com.mounanga.operationservice.dtos;

import com.mounanga.operationservice.enums.Currency;
import com.mounanga.operationservice.enums.Type;

import java.math.BigDecimal;
import java.util.Date;

public record OperationDTO(
        String id,
        Date date,
        Type type,
        String description,
        Currency currency,
        BigDecimal amount,
        String accountId
) {
}
