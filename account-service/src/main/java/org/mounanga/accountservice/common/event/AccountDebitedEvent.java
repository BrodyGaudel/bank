package org.mounanga.accountservice.common.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountDebitedEvent extends BaseEvent<String>{
    private final String description;
    private final BigDecimal amount;
    private final LocalDateTime dateTime;
    private final String debitedBy;

    public AccountDebitedEvent(String id, String description, BigDecimal amount, LocalDateTime dateTime, String debitedBy) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.debitedBy = debitedBy;
    }
}
