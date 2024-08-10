package org.mounanga.accountservice.commands.util.validation;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.common.event.AccountCreatedEvent;
import org.mounanga.accountservice.common.event.AccountDeletedEvent;
import org.springframework.stereotype.Component;


@Component
@ProcessingGroup("accountEmailCustomerId")
public class AccountEventHandler {

    @EventHandler
    public void on(@NotNull AccountCreatedEvent event, @NotNull AccountEmailCustomerIdRepository repository){
        AccountEmailCustomerId emailCustomerId = new AccountEmailCustomerId(
                event.getId(), event.getEmail(), event.getCustomerId()
        );
        repository.save(emailCustomerId);
    }

    @EventHandler
    public void on(@NotNull AccountDeletedEvent event, @NotNull AccountEmailCustomerIdRepository  repository){
        repository.deleteById(event.getId());
    }
}
