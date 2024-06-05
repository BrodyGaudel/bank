package org.mounanga.accountservice.common.event;

public class AccountDeletedEvent extends BaseEvent<String>{
    public AccountDeletedEvent(String id) {
        super(id);
    }
}
