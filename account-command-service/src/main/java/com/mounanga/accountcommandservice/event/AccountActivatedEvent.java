package com.mounanga.accountcommandservice.event;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountActivatedEvent extends BaseEvent<String> {

    @JsonCreator
    public AccountActivatedEvent(
            @JsonProperty("id") String id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("author") String author
    ) {
        super(id, dateTime, author);
    }
}

