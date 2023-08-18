package com.mounanga.operationservice.dtos;

import java.math.BigDecimal;

public record DebitDTO(String accountId, BigDecimal amount, String description) {
}
