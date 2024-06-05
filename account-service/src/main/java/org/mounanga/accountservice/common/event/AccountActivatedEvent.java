package org.mounanga.accountservice.common.event;

import lombok.Getter;
import org.mounanga.accountservice.common.enums.Status;

import java.time.LocalDateTime;

@Getter
public class AccountActivatedEvent extends BaseEvent<String>{
    private final Status status;
    private final LocalDateTime dateTime;
    private final String activatedBy;

    public AccountActivatedEvent(String id, Status status, LocalDateTime dateTime, String activatedBy) {
        super(id);
        this.status = status;
        this.dateTime = dateTime;
        this.activatedBy = activatedBy;
    }
}
