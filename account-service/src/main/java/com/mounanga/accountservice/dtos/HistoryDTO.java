package com.mounanga.accountservice.dtos;

import com.mounanga.accountservice.enums.Currency;

import java.math.BigDecimal;
import java.util.List;

public record HistoryDTO(
        String accountId,
        Currency currency,
        BigDecimal balance,
        int currentPage,
        int totalPages,
        int pageSize,
        List<OperationDTO> operations
) {
}
