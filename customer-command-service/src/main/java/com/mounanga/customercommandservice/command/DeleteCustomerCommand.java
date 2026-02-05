package com.mounanga.customercommandservice.command;

import java.time.LocalDateTime;

public class DeleteCustomerCommand extends BaseCommand<String>{
    public DeleteCustomerCommand(String id, LocalDateTime dateTime, String author) {
        super(id, dateTime, author);
    }
}
