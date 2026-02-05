package com.mounanga.customercommandservice.command;

import java.time.LocalDateTime;

public class RestoreCustomerCommand extends BaseCommand<String>{
    public RestoreCustomerCommand(String id, LocalDateTime dateTime, String author) {
        super(id, dateTime, author);
    }
}
