package org.mounanga.accountservice.commands.command;

import lombok.Getter;
import org.mounanga.accountservice.common.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class DebitAccountCommand extends BaseCommand<String> {
    private final BigDecimal amount;
    private final OperationType type;
    private final String description;

    public DebitAccountCommand(String id, LocalDateTime commandDate, String commandBy, BigDecimal amount, OperationType type, String description) {
        super(id, commandDate, commandBy);
        this.amount = amount;
        this.type = type;
        this.description = description;
    }
}
