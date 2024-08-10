package org.mounanga.accountservice.commands.aggregate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.commands.command.*;
import org.mounanga.accountservice.commands.exception.AccountNotActivatedException;
import org.mounanga.accountservice.commands.exception.AmountNotSufficientException;
import org.mounanga.accountservice.commands.exception.BalanceNotSufficientException;
import org.mounanga.accountservice.commands.exception.NotAuthorizedException;
import org.mounanga.accountservice.commands.util.factory.EventFactory;
import org.mounanga.accountservice.common.enums.AccountStatus;
import org.mounanga.accountservice.common.enums.Currency;
import org.mounanga.accountservice.common.event.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Aggregate
@Getter
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private String email;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;

    public AccountAggregate() {
        super();
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("CreateAccountCommand handled");
        AccountCreatedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountCreatedEvent event) {
        log.info("AccountCreatedEvent handled");
        this.accountId = event.getId();
        this.status = event.getStatus();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.customerId = event.getCustomerId();
        this.email = event.getEmail();
        this.createdBy = event.getEventBy();
        this.createdDate = event.getEventDate();
        AccountActivatedEvent accountActivatedEvent = EventFactory.create(this.accountId, this.createdDate, this.createdBy, AccountStatus.ACTIVATED);
        AggregateLifecycle.apply(accountActivatedEvent);
    }

    @CommandHandler
    public void handle(ActivateAccountCommand command) {
        log.info("AccountActivatedEvent command");
        AccountActivatedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountActivatedEvent event) {
        log.info("AccountActivatedEvent handled");
        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(SuspendAccountCommand command) {
        log.info("SuspendAccountCommand handled");
        AccountSuspendedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountSuspendedEvent event) {
        log.info("AccountSuspendedEvent handled");
        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(@NotNull CreditAccountCommand command) {
        log.info("CreditAccountCommand handled");
        if(command.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountNotSufficientException(" amount not sufficient => " + command.getAmount());
        }else if (!this.status.equals(AccountStatus.ACTIVATED)) {
            throw new AccountNotActivatedException("Account not activated => " + this.status);
        }else{
            AccountCreditedEvent event = EventFactory.create(command);
            AggregateLifecycle.apply(event);
        }
    }

    @EventSourcingHandler
    public void on(@NotNull AccountCreditedEvent event) {
        log.info("AccountCreditedEvent handled");
        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.balance = this.balance.add(event.getAmount());
    }

    @CommandHandler
    public void handle(@NotNull DebitAccountCommand command) {
        log.info("DebitAccountCommand handled");
        if (this.balance.compareTo(BigDecimal.ZERO) < 0 || this.balance.compareTo(command.getAmount()) < 0) {
            throw new BalanceNotSufficientException("Balance not sufficient => " + this.balance);
        } else if (!this.status.equals(AccountStatus.ACTIVATED)) {
            throw new AccountNotActivatedException("Account not activated => " + this.status);
        } else {
            AccountDebitedEvent event = EventFactory.create(command);
            AggregateLifecycle.apply(event);
        }
    }

    @EventSourcingHandler
    public void on(@NotNull AccountDebitedEvent event) {
        log.info("# AccountDebitedEvent handled");
        this.accountId = event.getId();
        this.balance = this.balance.subtract(event.getAmount());
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
    }

    @CommandHandler
    public void handle(@NotNull DeleteAccountCommand command) {
        log.info("DeleteAccountCommand handled");
        if (this.balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new NotAuthorizedException("Not authorized : The balance must be 0 in order to delete the account.=> " + this.balance);
        }
        AccountDeletedEvent event = EventFactory.create(command);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(@NotNull AccountDeletedEvent event) {
        log.info("AccountDeletedEvent handled");
        this.accountId = event.getId();
        this.lastModifiedBy = event.getEventBy();
        this.lastModifiedDate = event.getEventDate();
        this.status = AccountStatus.DELETED;
        this.email = null;
        this.customerId = null;
    }

}
