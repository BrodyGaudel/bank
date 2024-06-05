package org.mounanga.accountservice.query.web.restclient;

import org.mounanga.accountservice.query.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Notification(String accountId,
                           Type type,
                           BigDecimal amount,
                           String description,
                           LocalDateTime dateTime,
                           String customerId) {
}
