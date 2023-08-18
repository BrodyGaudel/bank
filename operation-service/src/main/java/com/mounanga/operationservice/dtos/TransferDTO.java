package com.mounanga.operationservice.dtos;

import java.math.BigDecimal;

public record TransferDTO(String accountIdSource,
                          String accountIdDestination,
                          String description,
                          BigDecimal amount) {
}
