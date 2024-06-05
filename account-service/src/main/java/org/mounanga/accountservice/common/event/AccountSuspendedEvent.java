package org.mounanga.accountservice.common.event;

import lombok.Getter;
import org.mounanga.accountservice.common.enums.Status;

import java.time.LocalDateTime;

@Getter
public class AccountSuspendedEvent extends BaseEvent<String>{
    private final Status status;
    private final LocalDateTime dateTime;
    private final String suspendedBy;

    public AccountSuspendedEvent(String id, Status status, LocalDateTime dateTime, String suspendedBy) {
        super(id);
        this.status = status;
        this.dateTime = dateTime;
        this.suspendedBy = suspendedBy;
    }
}
