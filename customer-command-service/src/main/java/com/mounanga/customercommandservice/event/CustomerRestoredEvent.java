package com.mounanga.customercommandservice.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CustomerRestoredEvent extends BaseEvent<String> {

    @JsonCreator
    public CustomerRestoredEvent(@JsonProperty("id") String id,
                                 @JsonProperty("dateTime") LocalDateTime dateTime,
                                 @JsonProperty("author") String author) {
        super(id, dateTime, author);
    }
}
