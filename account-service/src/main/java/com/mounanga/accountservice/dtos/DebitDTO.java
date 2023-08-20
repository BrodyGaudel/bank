package com.mounanga.accountservice.dtos;

import java.math.BigDecimal;

public record DebitDTO(String id, String description, BigDecimal amount) {
}
