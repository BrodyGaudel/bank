package com.mounanga.accountservice.dtos;

import java.math.BigDecimal;

public record UpdateBalanceForm(String accountId, BigDecimal amount) {
}
