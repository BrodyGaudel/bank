package com.mounanga.operationservice.dtos;

import java.math.BigDecimal;

public record CreditDTO(String accountId, BigDecimal amount, String description) {
}
