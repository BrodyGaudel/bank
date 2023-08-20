package com.mounanga.accountservice.dtos;

import java.math.BigDecimal;

public record CreditDTO(String id, String description, BigDecimal amount) {
}
