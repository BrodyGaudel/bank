package org.mounanga.accountservice.command.commands;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CreditAccountCommand extends BaseCommand<String> {
    private final String description;
    private final BigDecimal amount;
    private final LocalDateTime dateTime;
    private final String creditBy;

    public CreditAccountCommand(String id, String description, BigDecimal amount, LocalDateTime dateTime, String creditBy) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.creditBy = creditBy;
    }
}
