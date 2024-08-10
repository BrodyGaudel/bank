package org.mounanga.accountservice.commands.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;

@Getter
public class BaseCommand<T> {
    @TargetAggregateIdentifier
    private final T id;
    private final LocalDateTime commandDate;
    private final String commandBy;

    public BaseCommand(final T id, final LocalDateTime commandDate, final String commandBy) {
        this.id = id;
        this.commandDate = commandDate;
        this.commandBy = commandBy;
    }
}
