package org.mounanga.notificationservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Notification(String accountId,
                           Type type,
                           BigDecimal amount,
                           String description,
                           LocalDateTime dateTime,
                           String customerId) {
}