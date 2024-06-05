package org.mounanga.accountservice.command.commands;

import lombok.Getter;
import org.mounanga.accountservice.common.enums.Status;

import java.time.LocalDateTime;

@Getter
public class SuspendAccountCommand extends BaseCommand<String> {
    private final Status status;
    private final LocalDateTime dateTime;
    private final String suspendBy;

    public SuspendAccountCommand(String id, Status status, LocalDateTime dateTime, String suspendBy) {
        super(id);
        this.status = status;
        this.dateTime = dateTime;
        this.suspendBy = suspendBy;
    }
}
