package org.mounanga.accountservice.common.event;


import lombok.Getter;
import org.mounanga.accountservice.common.enums.AccountStatus;
import org.mounanga.accountservice.common.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountCreatedEvent extends BaseEvent<String> {
    private final AccountStatus status;
    private final BigDecimal balance;
    private final Currency currency;
    private final String customerId;
    private final String email;

    public AccountCreatedEvent(String id, LocalDateTime eventDate, String eventBy, AccountStatus status, BigDecimal balance, Currency currency, String customerId, String email) {
        super(id, eventDate, eventBy);
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.customerId = customerId;
        this.email = email;
    }
}
