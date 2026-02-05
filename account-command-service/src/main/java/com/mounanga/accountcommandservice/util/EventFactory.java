package com.mounanga.accountcommandservice.util;

import com.mounanga.accountcommandservice.command.*;
import com.mounanga.accountcommandservice.event.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Factory class for creating event objects from corresponding account commands.
 * This utility class follows the Event Sourcing pattern by transforming command objects into
 * event objects that represent state changes in the account domain.
 *
 * <p>Each method in this factory creates an immutable event object that captures
 * the intent of a command and will be persisted to the event store. The factory ensures
 * consistent event creation by copying all relevant data from commands to events.</p>
 *
 * <p>This class supports the full lifecycle of account operations including:
 * <ul>
 *   <li>Account creation and initialization</li>
 *   <li>Account credit and debit transactions</li>
 *   <li>Account status changes (activation/deactivation)</li>
 * </ul>
 *
 * <p>This class is not meant to be instantiated as it only contains static methods.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
public class EventFactory {

    /**
     * Private constructor to prevent instantiation.
     * This class should be used via its static methods only.
     */
    private EventFactory() {
        super();
    }

    /**
     * Creates an {@link AccountCreatedEvent} from a {@link CreateAccountCommand}.
     * This event represents the initial creation of an account in the system.
     *
     * @param command the command object containing account creation data
     * @return a new {@link AccountCreatedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull AccountCreatedEvent from(final @NotNull CreateAccountCommand command) {
        return new AccountCreatedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor(),
                command.getStatus(),
                command.getBalance(),
                command.getCurrency(),
                command.getCustomerId()
        );
    }

    /**
     * Creates an {@link AccountCreditedEvent} from a {@link CreditAccountCommand}.
     * This event represents a credit transaction that increases the account balance.
     *
     * @param command the command object containing account credit data
     * @return a new {@link AccountCreditedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull AccountCreditedEvent from(final @NotNull CreditAccountCommand command) {
        return new AccountCreditedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor(),
                command.getAmount(),
                command.getDescription(),
                command.getCurrency()
        );
    }

    /**
     * Creates an {@link AccountDebitedEvent} from a {@link DebitAccountCommand}.
     * This event represents a debit transaction that decreases the account balance.
     *
     * @param command the command object containing account debit data
     * @return a new {@link AccountDebitedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull AccountDebitedEvent from(final @NotNull DebitAccountCommand command) {
        return new AccountDebitedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor(),
                command.getAmount(),
                command.getDescription(),
                command.getCurrency()
        );
    }

    /**
     * Creates an {@link AccountActivatedEvent} from an {@link ActivateAccountCommand}.
     * This event represents the activation of a previously deactivated account.
     *
     * @param command the command object containing account activation data
     * @return a new {@link AccountActivatedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull AccountActivatedEvent from(final @NotNull ActivateAccountCommand command) {
        return new AccountActivatedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor()
        );
    }

    /**
     * Creates an {@link AccountDeactivatedEvent} from a {@link DeactivateAccountCommand}.
     * This event represents the deactivation of an active account.
     *
     * @param command the command object containing account deactivation data
     * @return a new {@link AccountDeactivatedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull AccountDeactivatedEvent from(final @NotNull DeactivateAccountCommand command) {
        return new AccountDeactivatedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor()
        );
    }
}