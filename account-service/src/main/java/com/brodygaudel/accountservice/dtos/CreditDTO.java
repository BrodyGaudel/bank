package com.brodygaudel.accountservice.dtos;

import java.math.BigDecimal;

public record CreditDTO(String accountId, String description, BigDecimal amount) {
}
