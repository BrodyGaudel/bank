package org.mounanga.accountservice.common.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BaseEvent<T> {
    private final T id;
    private final LocalDateTime eventDate;
    private final String eventBy;

    public BaseEvent(T id, LocalDateTime eventDate, String eventBy) {
        this.id = id;
        this.eventDate = eventDate;
        this.eventBy = eventBy;
    }
}
