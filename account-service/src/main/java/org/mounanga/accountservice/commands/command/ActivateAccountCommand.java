package org.mounanga.accountservice.commands.command;


import lombok.Getter;
import org.mounanga.accountservice.common.enums.AccountStatus;

import java.time.LocalDateTime;

@Getter
public class ActivateAccountCommand extends BaseCommand<String>{
    private final AccountStatus status;

    public ActivateAccountCommand(String id, LocalDateTime commandDate, String commandBy, AccountStatus status) {
        super(id, commandDate, commandBy);
        this.status = status;
    }
}
