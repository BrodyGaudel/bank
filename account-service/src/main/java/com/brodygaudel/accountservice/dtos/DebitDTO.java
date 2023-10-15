package com.brodygaudel.accountservice.dtos;

import java.math.BigDecimal;

public record DebitDTO(String accountId, String description, BigDecimal amount) {
}
