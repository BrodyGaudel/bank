package com.brodygaudel.accountservice.dto;

import java.math.BigDecimal;

public record CreditDTO(String accountId, String description, BigDecimal amount) {
}
