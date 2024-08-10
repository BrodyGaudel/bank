package org.mounanga.accountservice.commands.command;


import lombok.Getter;
import org.mounanga.accountservice.common.enums.AccountStatus;

import java.time.LocalDateTime;

@Getter
public class SuspendAccountCommand extends BaseCommand<String>{
    private final AccountStatus status;

    public SuspendAccountCommand(String id, LocalDateTime commandDate, String commandBy, AccountStatus status) {
        super(id, commandDate, commandBy);
        this.status = status;
    }
}
