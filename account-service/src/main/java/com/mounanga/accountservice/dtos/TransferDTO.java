package com.mounanga.accountservice.dtos;

import java.math.BigDecimal;

public record TransferDTO(String idFrom, String idTo, String description, BigDecimal amount) {
}
