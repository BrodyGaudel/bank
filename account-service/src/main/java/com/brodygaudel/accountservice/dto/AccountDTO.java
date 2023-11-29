package com.brodygaudel.accountservice.dto;

import com.brodygaudel.accountservice.enums.AccountStatus;
import com.brodygaudel.accountservice.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDTO(String id, String customerId, BigDecimal balance, Currency currency,
                         AccountStatus status, LocalDateTime creation, LocalDateTime lastUpdate) {
}
