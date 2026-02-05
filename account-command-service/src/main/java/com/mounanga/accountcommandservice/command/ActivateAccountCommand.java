package com.mounanga.accountcommandservice.command;

import java.time.LocalDateTime;

public class ActivateAccountCommand extends BaseCommand<String>{

    public ActivateAccountCommand(String id, LocalDateTime dateTime, String author) {
        super(id, dateTime, author);
    }
}
