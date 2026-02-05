package com.mounanga.accountcommandservice.aggregate;

import com.mounanga.accountcommandservice.command.*;
import com.mounanga.accountcommandservice.enums.AccountStatus;
import com.mounanga.accountcommandservice.enums.Currency;
import com.mounanga.accountcommandservice.event.*;
import com.mounanga.accountcommandservice.exception.AccountNotActivatedException;
import com.mounanga.accountcommandservice.exception.AmountNotSufficientException;
import com.mounanga.accountcommandservice.exception.BalanceNotSufficientException;
import com.mounanga.accountcommandservice.security.SecurityUtils;
import com.mounanga.accountcommandservice.util.EventFactory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Aggregate root for Account domain.
 * This aggregate handles account-related commands and applies corresponding events
 * using Event Sourcing pattern with Axon Framework.
 *
 * <p>Business rules enforced:
 * <ul>
 *   <li>Credit and debit operations require an activated account</li>
 *   <li>Credit amount must be positive</li>
 *   <li>Debit amount must not exceed current balance</li>
 *   <li>Account is automatically activated upon creation</li>
 * </ul>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Aggregate
@Getter
@NoArgsConstructor
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private AccountStatus status;
    private BigDecimal balance;
    private Currency currency;
    private String customerId;
    private LocalDateTime auditDateTime;
    private String auditAuthor;

    /**
     * Command handler for creating a new account.
     * Automatically activates the account upon creation.
     *
     * @param command the CreateAccountCommand containing account details
     */
    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("Handling CreateAccountCommand for account with id: {}", command.getId());
        AccountCreatedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("AccountCreatedEvent applied for account id: {}", command.getId());
    }

    /**
     * Command handler for activating an account.
     * Validates that the account is not already activated.
     *
     * @param command the ActivateAccountCommand
     * @throws IllegalStateException if the account is already activated
     */
    @CommandHandler
    public void handle(@NotNull ActivateAccountCommand command) {
        log.info("Handling ActivateAccountCommand for account with id: {}", command.getId());

        if (AccountStatus.ACTIVATED.equals(this.status)) {
            log.warn("Attempted to activate already activated account with id: {}", command.getId());
            throw new IllegalStateException("Account is already activated");
        }

        AccountActivatedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("AccountActivatedEvent applied for account id: {}", command.getId());
    }

    /**
     * Command handler for deactivating an account.
     * Validates that the account is not already deactivated.
     *
     * @param command the DeactivateAccountCommand
     * @throws IllegalStateException if the account is already deactivated
     */
    @CommandHandler
    public void handle(@NotNull DeactivateAccountCommand command) {
        log.info("Handling DeactivateAccountCommand for account with id: {}", command.getId());

        if (AccountStatus.DEACTIVATED.equals(this.status)) {
            log.warn("Attempted to deactivate already deactivated account with id: {}", command.getId());
            throw new IllegalStateException("Account is already deactivated");
        }

        AccountDeactivatedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("AccountDeactivatedEvent applied for account id: {}", command.getId());
    }

    /**
     * Command handler for crediting an account.
     * Validates that:
     * <ul>
     *   <li>The amount is positive</li>
     *   <li>The account is activated</li>
     * </ul>
     *
     * @param command the CreditAccountCommand containing the amount to credit
     * @throws AmountNotSufficientException if the amount is not positive
     * @throws AccountNotActivatedException if the account is not activated
     */
    @CommandHandler
    public void handle(@NotNull CreditAccountCommand command) {
        log.info("Handling CreditAccountCommand for account with id: {}, amount: {}", command.getId(), command.getAmount());

        if (command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Credit failed: Invalid amount {} for account id: {}", command.getAmount(), command.getId());
            throw new AmountNotSufficientException("Amount must be positive: " + command.getAmount());
        }

        if (!AccountStatus.ACTIVATED.equals(this.status)) {
            log.warn("Credit failed: Account not activated. Account id: {}, status: {}", command.getId(), this.status);
            throw new AccountNotActivatedException("Account not activated. Current status: " + this.status);
        }

        AccountCreditedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("AccountCreditedEvent applied for account id: {}, new balance will be: {}", command.getId(), this.balance.add(command.getAmount()));
    }

    /**
     * Command handler for debiting an account.
     * Validates that:
     * <ul>
     *   <li>The amount is positive</li>
     *   <li>The balance is sufficient</li>
     *   <li>The account is activated</li>
     * </ul>
     *
     * @param command the DebitAccountCommand containing the amount to debit
     * @throws AmountNotSufficientException if the amount is not positive
     * @throws BalanceNotSufficientException if the balance is insufficient
     * @throws AccountNotActivatedException if the account is not activated
     */
    @CommandHandler
    public void handle(@NotNull DebitAccountCommand command) {
        log.info("Handling DebitAccountCommand for account with id: {}, amount: {}", command.getId(), command.getAmount());

        if (command.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Debit failed: Invalid amount {} for account id: {}",command.getAmount(), command.getId());
            throw new AmountNotSufficientException("Amount must be positive: " + command.getAmount());
        }

        if (this.balance.compareTo(command.getAmount()) < 0) {
            log.warn("Debit failed: Insufficient balance. Account id: {}, current balance: {}, requested amount: {}", command.getId(), this.balance, command.getAmount());
            throw new BalanceNotSufficientException(
                    String.format("Insufficient balance. Current: %s, Requested: %s",
                            this.balance, command.getAmount()));
        }

        if (!AccountStatus.ACTIVATED.equals(this.status)) {log.warn("Debit failed: Account not activated. Account id: {}, status: {}", command.getId(), this.status);
            throw new AccountNotActivatedException("Account not activated. Current status: " + this.status);
        }

        AccountDebitedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("AccountDebitedEvent applied for account id: {}, new balance will be: {}", command.getId(), this.balance.subtract(command.getAmount()));
    }

    /**
     * Event sourcing handler for AccountCreatedEvent.
     * Initializes the account state and automatically triggers activation.
     *
     * @param event the AccountCreatedEvent
     */
    @EventSourcingHandler
    public void on(@NotNull AccountCreatedEvent event) {
        log.debug("Applying AccountCreatedEvent for account id: {}", event.getId());
        this.accountId = event.getId();
        this.status = event.getStatus();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.customerId = event.getCustomerId();
        this.auditDateTime = event.getDateTime();
        this.auditAuthor = event.getAuthor();

        log.info("Account created with id: {}, customer: {}, initial balance: {} {}", this.accountId, this.customerId, this.balance, this.currency);

        // Automatically activate the account upon creation
        ActivateAccountCommand activateCommand = new ActivateAccountCommand(
                this.accountId,
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername()
        );
        AccountActivatedEvent activatedEvent = EventFactory.from(activateCommand);
        AggregateLifecycle.apply(activatedEvent);

        log.debug("Auto-activation triggered for account id: {}", this.accountId);
    }

    /**
     * Event sourcing handler for AccountActivatedEvent.
     * Updates the account status to ACTIVATE.
     *
     * @param event the AccountActivatedEvent
     */
    @EventSourcingHandler
    public void on(@NotNull AccountActivatedEvent event) {
        log.debug("Applying AccountActivatedEvent for account id: {}", event.getId());
        this.accountId = event.getId();
        this.auditDateTime = event.getDateTime();
        this.auditAuthor = event.getAuthor();
        this.status = AccountStatus.ACTIVATED;
        log.info("Account activated with id: {}, author: {}", this.accountId, this.auditAuthor);
    }

    /**
     * Event sourcing handler for AccountDeactivatedEvent.
     * Updates the account status to DEACTIVATE.
     *
     * @param event the AccountDeactivatedEvent
     */
    @EventSourcingHandler
    public void on(@NotNull AccountDeactivatedEvent event) {
        log.debug("Applying AccountDeactivatedEvent for account id: {}", event.getId());
        this.accountId = event.getId();
        this.auditDateTime = event.getDateTime();
        this.auditAuthor = event.getAuthor();
        this.status = AccountStatus.DEACTIVATED;
        log.info("Account deactivated with id: {}, author: {}", this.accountId, this.auditAuthor);
    }

    /**
     * Event sourcing handler for AccountCreditedEvent.
     * Increases the account balance by the credited amount.
     *
     * @param event the AccountCreditedEvent
     */
    @EventSourcingHandler
    public void on(@NotNull AccountCreditedEvent event) {
        log.debug("Applying AccountCreditedEvent for account id: {}, amount: {}", event.getId(), event.getAmount());
        this.accountId = event.getId();
        this.auditDateTime = event.getDateTime();
        this.auditAuthor = event.getAuthor();

        BigDecimal oldBalance = this.balance;
        this.balance = this.balance.add(event.getAmount());

        log.info("Account credited. Account id: {}, amount: {}, old balance: {}, new balance: {}, author: {}", this.accountId, event.getAmount(), oldBalance, this.balance, this.auditAuthor);
    }

    /**
     * Event sourcing handler for AccountDebitedEvent.
     * Decreases the account balance by the debited amount.
     *
     * @param event the AccountDebitedEvent
     */
    @EventSourcingHandler
    public void on(@NotNull AccountDebitedEvent event) {
        log.debug("Applying AccountDebitedEvent for account id: {}, amount: {}",
                event.getId(), event.getAmount());
        this.accountId = event.getId();
        this.auditDateTime = event.getDateTime();
        this.auditAuthor = event.getAuthor();

        BigDecimal oldBalance = this.balance;
        this.balance = this.balance.subtract(event.getAmount());

        log.info("Account debited. Account id: {}, amount: {}, old balance: {}, new balance: {}, author: {}", this.accountId, event.getAmount(), oldBalance, this.balance, this.auditAuthor);
    }
}

