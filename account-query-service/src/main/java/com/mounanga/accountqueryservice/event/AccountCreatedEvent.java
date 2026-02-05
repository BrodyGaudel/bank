package com.mounanga.accountqueryservice.event;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mounanga.accountqueryservice.enums.AccountStatus;
import com.mounanga.accountqueryservice.enums.Currency;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountCreatedEvent extends BaseEvent<String> {

    private final AccountStatus status;
    private final BigDecimal balance;
    private final Currency currency;
    private final String customerId;

    @JsonCreator
    public AccountCreatedEvent(
            @JsonProperty("id") String id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("author") String author,
            @JsonProperty("status") AccountStatus status,
            @JsonProperty("balance") BigDecimal balance,
            @JsonProperty("currency") Currency currency,
            @JsonProperty("customerId") String customerId
    ) {
        super(id, dateTime, author);
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.customerId = customerId;
    }
}
