package com.mounanga.accountcommandservice.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mounanga.accountcommandservice.enums.Currency;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class AccountCreditedEvent extends BaseEvent<String> {

    private final BigDecimal amount;
    private final String description;
    private final Currency currency;

    @JsonCreator
    public AccountCreditedEvent(
            @JsonProperty("id") String id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("author") String author,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("description") String description,
            @JsonProperty("currency") Currency currency
    ) {
        super(id, dateTime, author);
        this.amount = amount;
        this.description = description;
        this.currency = currency;
    }
}

