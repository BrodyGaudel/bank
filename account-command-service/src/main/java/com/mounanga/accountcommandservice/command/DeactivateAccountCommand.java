package com.mounanga.accountcommandservice.command;

import java.time.LocalDateTime;

public class DeactivateAccountCommand extends BaseCommand<String>{

    public DeactivateAccountCommand(String id, LocalDateTime dateTime, String author) {
        super(id, dateTime, author);
    }
}
