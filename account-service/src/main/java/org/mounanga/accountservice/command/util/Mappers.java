package org.mounanga.accountservice.command.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.command.commands.*;
import org.mounanga.accountservice.common.event.*;

public class Mappers {

    private Mappers() {
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountCreatedEvent fromCreateAccountCommand(@NotNull final CreateAccountCommand command) {
        return new AccountCreatedEvent(
                command.getId(),
                command.getStatus(),
                command.getBalance(),
                command.getCurrency(),
                command.getCustomerId(),
                command.getDateTime(),
                command.getCreateBy()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountDebitedEvent fromDebitAccountCommand(@NotNull final DebitAccountCommand command){
        return new AccountDebitedEvent(
                command.getId(),
                command.getDescription(),
                command.getAmount(),
                command.getDateTime(),
                command.getDebitBy()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountCreditedEvent fromCreditAccountCommand(@NotNull final CreditAccountCommand command){
        return new AccountCreditedEvent(
                command.getId(),
                command.getDescription(),
                command.getAmount(),
                command.getDateTime(),
                command.getCreditBy()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountActivatedEvent fromActivateAccountCommand(@NotNull final ActivateAccountCommand command){
        return new AccountActivatedEvent(
                command.getId(),
                command.getStatus(),
                command.getDateTime(),
                command.getActivateBy()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountSuspendedEvent fromSuspendAccountCommand(@NotNull final SuspendAccountCommand command){
        return new AccountSuspendedEvent(
                command.getId(),
                command.getStatus(),
                command.getDateTime(),
                command.getSuspendBy()
        );
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountDeletedEvent fromDeleteAccountCommand(@NotNull final DeleteAccountCommand command){
        return new AccountDeletedEvent(command.getId());
    }
}
