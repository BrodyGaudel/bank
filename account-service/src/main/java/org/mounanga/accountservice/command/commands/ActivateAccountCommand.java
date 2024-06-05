package org.mounanga.accountservice.command.commands;

import lombok.Getter;
import org.mounanga.accountservice.common.enums.Status;

import java.time.LocalDateTime;

@Getter
public class ActivateAccountCommand extends BaseCommand<String> {

    private final Status status;
    private final LocalDateTime dateTime;
    private final String activateBy;

    public ActivateAccountCommand(String id, Status status, LocalDateTime dateTime, String activateBy) {
        super(id);
        this.status = status;
        this.dateTime = dateTime;
        this.activateBy = activateBy;
    }
}
