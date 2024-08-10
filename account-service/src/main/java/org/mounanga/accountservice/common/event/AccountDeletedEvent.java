package org.mounanga.accountservice.common.event;

import java.time.LocalDateTime;

public class AccountDeletedEvent extends BaseEvent<String> {

    public AccountDeletedEvent(String id, LocalDateTime eventDate, String eventBy) {
        super(id, eventDate, eventBy);
    }
}
