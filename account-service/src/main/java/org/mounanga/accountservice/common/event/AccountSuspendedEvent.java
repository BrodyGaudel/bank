package org.mounanga.accountservice.common.event;


import lombok.Getter;
import org.mounanga.accountservice.common.enums.AccountStatus;

import java.time.LocalDateTime;

@Getter
public class AccountSuspendedEvent extends BaseEvent<String> {
    private final AccountStatus status;

    public AccountSuspendedEvent(String id, LocalDateTime eventDate, String eventBy, AccountStatus status) {
        super(id, eventDate, eventBy);
        this.status = status;
    }
}