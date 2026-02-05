package com.mounanga.accountcommandservice.event;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AccountDeactivatedEvent extends BaseEvent<String> {

    @JsonCreator
    public AccountDeactivatedEvent(
            @JsonProperty("id") String id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("author") String author
    ) {
        super(id, dateTime, author);
    }
}

