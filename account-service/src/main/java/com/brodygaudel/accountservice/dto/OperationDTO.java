package com.brodygaudel.accountservice.dto;

import com.brodygaudel.accountservice.enums.Currency;
import com.brodygaudel.accountservice.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationDTO(String id, LocalDateTime date, OperationType type, BigDecimal amount,
                           Currency currency, String description, String accountId) {
}
