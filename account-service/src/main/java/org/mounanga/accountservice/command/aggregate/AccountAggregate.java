package org.mounanga.accountservice.command.aggregate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.command.commands.*;
import org.mounanga.accountservice.common.enums.Currency;
import org.mounanga.accountservice.common.enums.Status;
import org.mounanga.accountservice.command.exception.AccountNotActivatedException;
import org.mounanga.accountservice.command.exception.AmountNotSufficientException;
import org.mounanga.accountservice.command.exception.BalanceNotSufficientException;
import org.mounanga.accountservice.command.util.Mappers;
import org.mounanga.accountservice.common.event.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Aggregate
@Slf4j
@Getter
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private Status status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;

    public AccountAggregate(){
        super();
    }

    @CommandHandler
    public AccountAggregate(@NotNull CreateAccountCommand command) {
        log.info("# CreateAccountCommand handled");
        AccountCreatedEvent event = Mappers.fromCreateAccountCommand(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountCreatedEvent event) {
        log.info("# AccountCreatedEvent handled");
        this.accountId = event.getId();
        this.status = event.getStatus();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.customerId = event.getCustomerId();
        this.createdDate = event.getDateTime();
        this.createdBy = event.getCreatedBy();
        AggregateLifecycle.apply(new AccountActivatedEvent(
                this.accountId, Status.ACTIVATED, LocalDateTime.now(), this.createdBy
        ));
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        log.info("# DebitAccountCommand handled");
        if (this.balance.compareTo(BigDecimal.ZERO) < 0 || this.balance.compareTo(command.getAmount()) < 0) {
            throw new BalanceNotSufficientException("Balance not sufficient => " + this.balance);
        } else if (!this.status.equals(Status.ACTIVATED)) {
            throw new AccountNotActivatedException("Account not activated => " + this.status);
        } else {
            AccountDebitedEvent event = Mappers.fromDebitAccountCommand(command);
            AggregateLifecycle.apply(event);
        }
    }

    @EventSourcingHandler
    public void on(@NotNull AccountDebitedEvent event) {
        log.info("# AccountDebitedEvent handled");
        this.accountId = event.getId();
        this.balance = this.balance.subtract(event.getAmount());
        this.lastModifiedBy = event.getDebitedBy();
        this.lastModifiedDate = event.getDateTime();
    }

    @CommandHandler
    public void handle(@NotNull CreditAccountCommand command) {
        log.info("# CreditAccountCommand handled");
        if(command.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountNotSufficientException(" amount not sufficient => " + command.getAmount());
        }else if (!this.status.equals(Status.ACTIVATED)) {
            throw new AccountNotActivatedException("Account not activated => " + this.status);
        }else{
            AccountCreditedEvent event = Mappers.fromCreditAccountCommand(command);
            AggregateLifecycle.apply(event);
        }
    }

    @EventSourcingHandler
    public void on(@NotNull AccountCreditedEvent event) {
        log.info("# AccountCreditedEvent handled");
        this.accountId = event.getId();
        this.balance = this.balance.add(event.getAmount());
        this.lastModifiedBy = event.getCreditedBy();
        this.lastModifiedDate = event.getDateTime();
    }

    @CommandHandler
    public void handle(@NotNull ActivateAccountCommand command) {
        log.info("# ActivateAccountCommand handled");
        AccountActivatedEvent event = Mappers.fromActivateAccountCommand(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountActivatedEvent event) {
        log.info("# AccountActivatedEvent handled");
        this.accountId = event.getId();
        this.status = event.getStatus();
        this.lastModifiedBy = event.getActivatedBy();
        this.lastModifiedDate = event.getDateTime();
    }

    @CommandHandler
    public void handle(@NotNull SuspendAccountCommand command) {
        log.info("# SuspendAccountCommand handled");
        AccountSuspendedEvent event = Mappers.fromSuspendAccountCommand(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountSuspendedEvent event) {
        log.info("# AccountSuspendedEvent handled");
        this.accountId = event.getId();
        this.status = event.getStatus();
        this.lastModifiedBy = event.getSuspendedBy();
        this.lastModifiedDate = event.getDateTime();
    }

    @CommandHandler
    public void handle(@NotNull DeleteAccountCommand command) {
        log.info("# DeleteAccountCommand handled");
        AccountDeletedEvent event = Mappers.fromDeleteAccountCommand(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountDeletedEvent event) {
        log.info("# AccountDeletedEvent handled");
        this.accountId = event.getId();
    }
}
