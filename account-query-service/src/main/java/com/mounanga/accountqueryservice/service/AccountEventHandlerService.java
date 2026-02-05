package com.mounanga.accountqueryservice.service;

import com.mounanga.accountqueryservice.event.*;
import ga.aninf.accountqueryservice.event.*;

/**
 * Service interface for handling account-related events in the Event Sourcing architecture.
 * This interface defines the contract for processing various account events that occur
 * within the system, enabling the projection of events into read models or external systems.
 *
 * <p>Implementations of this interface are responsible for:
 * <ul>
 *   <li>Updating account projections for query operations</li>
 *   <li>Integrating with external systems via event-driven patterns</li>
 *   <li>Maintaining consistency between the event store and query models</li>
 *   <li>Executing side effects triggered by account state changes</li>
 * </ul>
 *
 * <p>Each method corresponds to a specific event type in the account domain and is
 * invoked when the corresponding event is persisted to the event store. Implementations
 * should be idempotent and handle events in a thread-safe manner.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
public interface AccountEventHandlerService {

    /**
     * Handles an {@link AccountCreatedEvent} when a new account is created in the system.
     * This method is triggered after an account creation command has been successfully processed.
     *
     * @param event the account created event containing account creation data
     * @throws NullPointerException if {@code event} is null
     */
    void handle(AccountCreatedEvent event);

    /**
     * Handles an {@link AccountActivatedEvent} when an account is activated.
     * This method is triggered after an account activation command has been successfully processed.
     *
     * @param event the account activated event containing account activation data
     * @throws NullPointerException if {@code event} is null
     */
    void handle(AccountActivatedEvent event);

    /**
     * Handles an {@link AccountDeactivatedEvent} when an account is deactivated.
     * This method is triggered after an account deactivation command has been successfully processed.
     *
     * @param event the account deactivated event containing account deactivation data
     * @throws NullPointerException if {@code event} is null
     */
    void handle(AccountDeactivatedEvent event);

    /**
     * Handles an {@link AccountCreditedEvent} when an account receives a credit.
     * This method is triggered after a credit account command has been successfully processed.
     *
     * @param event the account credited event containing credit transaction data
     * @throws NullPointerException if {@code event} is null
     */
    void handle(AccountCreditedEvent event);

    /**
     * Handles an {@link AccountDebitedEvent} when an account is debited.
     * This method is triggered after a debit account command has been successfully processed.
     *
     * @param event the account debited event containing debit transaction data
     * @throws NullPointerException if {@code event} is null
     */
    void handle(AccountDebitedEvent event);
}
