package org.mounanga.accountservice.common.event;

import lombok.Getter;

@Getter
public abstract class BaseEvent<T> {
    private final T id;

    protected BaseEvent(T id) {
        this.id = id;
    }
}
