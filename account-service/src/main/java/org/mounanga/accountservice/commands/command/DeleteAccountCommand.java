package org.mounanga.accountservice.commands.command;

import java.time.LocalDateTime;

public class DeleteAccountCommand extends BaseCommand<String> {

    public DeleteAccountCommand(String id, LocalDateTime commandDate, String commandBy) {
        super(id, commandDate, commandBy);
    }
}
