package org.mounanga.accountservice.common.event;

import lombok.Getter;
import org.mounanga.accountservice.common.enums.Currency;
import org.mounanga.accountservice.common.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountCreatedEvent extends BaseEvent<String>{
    private final Status status;
    private final BigDecimal balance;
    private final Currency currency;
    private final String customerId;
    private final LocalDateTime dateTime;
    private final String createdBy;

    public AccountCreatedEvent(String id, Status status, BigDecimal balance, Currency currency, String customerId, LocalDateTime dateTime, String createdBy) {
        super(id);
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.customerId = customerId;
        this.dateTime = dateTime;
        this.createdBy = createdBy;
    }
}
