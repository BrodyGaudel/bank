package com.mounanga.customerqueryservice.event;

import lombok.Getter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for all customer events.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Getter
public abstract class BaseEvent<T> {
    private final T id;
    private final LocalDateTime dateTime;
    private final String author;

    @JsonCreator
    protected BaseEvent(
            @JsonProperty("id") T id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("author") String author) {
        this.id = id;
        this.dateTime = dateTime;
        this.author = author;
    }
}
