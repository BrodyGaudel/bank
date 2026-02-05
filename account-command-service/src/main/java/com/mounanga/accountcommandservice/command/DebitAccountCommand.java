package com.mounanga.accountcommandservice.command;

import com.mounanga.accountcommandservice.enums.Currency;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class DebitAccountCommand extends BaseCommand<String>{
    private final BigDecimal amount;
    private final String description;
    private final Currency currency;

    public DebitAccountCommand(String id, LocalDateTime dateTime, String author, BigDecimal amount, String description, Currency currency) {
        super(id, dateTime, author);
        this.amount = amount;
        this.description = description;
        this.currency = currency;
    }
}
