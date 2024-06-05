package org.mounanga.accountservice.command.commands;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class DebitAccountCommand extends BaseCommand<String>{
    private final String description;
    private final BigDecimal amount;
    private final LocalDateTime dateTime;
    private final String debitBy;

    public DebitAccountCommand(String id, String description, BigDecimal amount, LocalDateTime dateTime, String debitBy) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
        this.debitBy = debitBy;
    }
}
