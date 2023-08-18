package com.mounanga.accountservice.dtos;

import com.mounanga.accountservice.enums.Currency;

public record UpdateCurrencyForm(String accountId, Currency currency) {
}
