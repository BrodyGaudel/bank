package org.mounanga.accountservice.common.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountCreditedEvent extends BaseEvent<String>{
    private final String description;
    private final BigDecimal amount;
    private final LocalDateTime dateTime;
    private final String creditedBy;

    public AccountCreditedEvent(String id, String description, BigDecimal amount, LocalDateTime dateTime, String creditedBy) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.creditedBy = creditedBy;
    }
}
