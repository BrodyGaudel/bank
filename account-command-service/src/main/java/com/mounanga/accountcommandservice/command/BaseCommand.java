package com.mounanga.accountcommandservice.command;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;

@Getter
public class BaseCommand<T> {

    @TargetAggregateIdentifier
    private final T id;
    private final LocalDateTime dateTime;
    private final String author;

    public BaseCommand(T id, LocalDateTime dateTime, String author) {
        this.id = id;
        this.dateTime = dateTime;
        this.author = author;
    }
}
