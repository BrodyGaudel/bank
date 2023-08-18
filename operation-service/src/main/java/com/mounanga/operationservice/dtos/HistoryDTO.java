package com.mounanga.operationservice.dtos;

import java.math.BigDecimal;
import java.util.List;

public record HistoryDTO(
        String accountId,
        BigDecimal balance,
        int currentPage,
        int totalPages,
        int pageSize,
        List<OperationDTO> operationDTOS) {
}
