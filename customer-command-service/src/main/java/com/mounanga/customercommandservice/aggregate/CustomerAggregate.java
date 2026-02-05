package com.mounanga.customercommandservice.aggregate;

import com.mounanga.customercommandservice.command.CreateCustomerCommand;
import com.mounanga.customercommandservice.command.DeleteCustomerCommand;
import com.mounanga.customercommandservice.command.RestoreCustomerCommand;
import com.mounanga.customercommandservice.command.UpdateCustomerCommand;
import com.mounanga.customercommandservice.enums.Gender;
import com.mounanga.customercommandservice.event.CustomerCreatedEvent;
import com.mounanga.customercommandservice.event.CustomerDeletedEvent;
import com.mounanga.customercommandservice.event.CustomerRestoredEvent;
import com.mounanga.customercommandservice.event.CustomerUpdatedEvent;
import com.mounanga.customercommandservice.util.EventFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Customer Aggregate representing the customer entity in the Event Sourcing and CQRS architecture.
 * This aggregate is responsible for handling commands, validating business rules, and emitting events
 * that represent state changes. It serves as the authoritative source for customer state changes.
 *
 * <p>The aggregate follows the principles of Domain-Driven Design (DDD) and Event Sourcing:
 * <ul>
 *   <li>Commands are validated and result in events</li>
 *   <li>Events are applied to update the aggregate's state</li>
 *   <li>The aggregate's state can be rebuilt by replaying all events</li>
 *   <li>Business invariants are enforced within the aggregate</li>
 * </ul>
 *
 * <p>This aggregate implements soft delete functionality by maintaining an {@code isDeleted} flag
 * rather than physically removing the aggregate from the event store.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Aggregate
@Getter
@Slf4j
public class CustomerAggregate {

    /**
     * Unique identifier for the customer aggregate.
     * This field is annotated with {@link AggregateIdentifier} to mark it as the aggregate's identifier
     * in the Axon Framework. It must be unique across all customer aggregates.
     */
    @AggregateIdentifier
    private String customerId;

    /**
     * Customer's first name.
     */
    private String firstname;

    /**
     * Customer's last name.
     */
    private String lastname;

    /**
     * Customer's place of birth.
     */
    private String placeOfBirth;

    /**
     * Customer's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * Customer's gender.
     */
    private Gender gender;

    /**
     * Customer's National Identification Number (NIP).
     */
    private String nip;

    /**
     * Customer's email address.
     */
    private String email;

    /**
     * Customer's nationality.
     */
    private String nationality;

    /**
     * Date and time when the last change was made to this aggregate.
     * This field is updated with each event to provide an audit trail.
     */
    private LocalDateTime auditDateTime;

    /**
     * Author (user) who performed the last change to this aggregate.
     * This field is populated from the security context and provides accountability.
     */
    private String auditAuthor;

    /**
     * Flag indicating whether the customer has been logically deleted.
     * When true, the customer is considered deleted and cannot be modified.
     * This implements soft delete functionality.
     */
    private Boolean isDeleted;

    /**
     * Default constructor required by Axon Framework.
     * This constructor is used when rebuilding the aggregate from its events.
     */
    public CustomerAggregate() {
        super();
    }

    /**
     * Command handler for creating a new customer.
     * This constructor handles {@link CreateCustomerCommand} by creating and applying
     * a corresponding {@link CustomerCreatedEvent}.
     *
     * <p>This is the initial command that creates a new customer aggregate instance.</p>
     *
     * @param command the command containing customer creation data
     * @throws NullPointerException if {@code command} is null
     */
    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand command) {
        log.info("Handling CreateCustomerCommand for customer with id: {}", command.getId());
        CustomerCreatedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("CustomerCreatedEvent applied for customer id: {}", command.getId());
    }

    /**
     * Command handler for updating an existing customer.
     * Validates that the customer is not deleted before allowing the update.
     *
     * @param command the command containing updated customer data
     * @throws IllegalStateException if the customer is already deleted
     * @throws NullPointerException if {@code command} is null
     */
    @CommandHandler
    public void handle(@NotNull UpdateCustomerCommand command) {
        log.info("Handling UpdateCustomerCommand for customer with id: {}", command.getId());
        if (Boolean.TRUE.equals(this.isDeleted)) {
            log.warn("Attempted to update deleted customer with id: {}", command.getId());
            throw new IllegalStateException("Cannot update a deleted customer");
        }
        CustomerUpdatedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("CustomerUpdatedEvent applied for customer id: {}", command.getId());
    }

    /**
     * Command handler for restoring a previously deleted customer.
     * Validates that the customer is currently deleted before allowing restoration.
     *
     * @param command the command containing customer restoration information
     * @throws IllegalStateException if the customer is already active (not deleted)
     * @throws NullPointerException if {@code command} is null
     */
    @CommandHandler
    public void handle(@NotNull RestoreCustomerCommand command) {
        log.info("Handling RestoreCustomerCommand for customer with id: {}", command.getId());
        if (Boolean.FALSE.equals(this.isDeleted)) {
            log.warn("Attempted to restore already active customer with id: {}", command.getId());
            throw new IllegalStateException("Customer is already active");
        }
        CustomerRestoredEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("CustomerRestoredEvent applied for customer id: {}", command.getId());
    }

    /**
     * Command handler for deleting a customer.
     * Validates that the customer is not already deleted before allowing the deletion.
     *
     * @param command the command containing customer deletion information
     * @throws IllegalStateException if the customer is already deleted
     * @throws NullPointerException if {@code command} is null
     */
    @CommandHandler
    public void handle(@NotNull DeleteCustomerCommand command) {
        log.info("Handling DeleteCustomerCommand for customer with id: {}", command.getId());
        if (Boolean.TRUE.equals(this.isDeleted)) {
            log.warn("Attempted to delete already deleted customer with id: {}", command.getId());
            throw new IllegalStateException("Customer is already deleted");
        }
        CustomerDeletedEvent event = EventFactory.from(command);
        AggregateLifecycle.apply(event);
        log.debug("CustomerDeletedEvent applied for customer id: {}", command.getId());
    }

    /**
     * Event sourcing handler for {@link CustomerCreatedEvent}.
     * Applies the initial state of the customer aggregate from the creation event.
     *
     * @param event the event containing customer creation data
     * @throws NullPointerException if {@code event} is null
     */
    @EventSourcingHandler
    public void on(@NotNull CustomerCreatedEvent event) {
        log.debug("Applying CustomerCreatedEvent for customer id: {}", event.getId());
        this.customerId = event.getId();
        this.firstname = event.getFirstname();
        this.lastname = event.getLastname();
        this.placeOfBirth = event.getPlaceOfBirth();
        this.dateOfBirth = event.getDateOfBirth();
        this.gender = event.getGender();
        this.nip = event.getNip();
        this.email = event.getEmail();
        this.nationality = event.getNationality();
        this.auditDateTime = event.getDateTime();
        this.auditAuthor = event.getAuthor();
        this.isDeleted = Boolean.FALSE;
        log.info("Customer created with id: {}, email: {}", this.customerId, this.email);
    }

    /**
     * Event sourcing handler for {@link CustomerUpdatedEvent}.
     * Updates the state of the customer aggregate with new data from the update event.
     *
     * @param event the event containing updated customer data
     * @throws NullPointerException if {@code event} is null
     */
    @EventSourcingHandler
    public void on(@NotNull CustomerUpdatedEvent event) {
        log.debug("Applying CustomerUpdatedEvent for customer id: {}", event.getId());
        this.customerId = event.getId();
        this.firstname = event.getFirstname();
        this.lastname = event.getLastname();
        this.placeOfBirth = event.getPlaceOfBirth();
        this.dateOfBirth = event.getDateOfBirth();
        this.gender = event.getGender();
        this.nip = event.getNip();
        this.email = event.getEmail();
        this.nationality = event.getNationality();
        this.auditDateTime = event.getDateTime();
        this.auditAuthor = event.getAuthor();
        this.isDeleted = Boolean.FALSE;
        log.info("Customer updated with id: {}, author: {}", this.customerId, this.auditAuthor);
    }

    /**
     * Event sourcing handler for {@link CustomerDeletedEvent}.
     * Marks the customer aggregate as deleted by setting the {@code isDeleted} flag to true.
     *
     * @param event the event containing customer deletion information
     * @throws NullPointerException if {@code event} is null
     */
    @EventSourcingHandler
    public void on(@NotNull CustomerDeletedEvent event) {
        log.debug("Applying CustomerDeletedEvent for customer id: {}", event.getId());
        this.customerId = event.getId();
        this.auditAuthor = event.getAuthor();
        this.auditDateTime = event.getDateTime();
        this.isDeleted = Boolean.TRUE;
        log.info("Customer deleted with id: {}, author: {}", this.customerId, this.auditAuthor);
    }

    /**
     * Event sourcing handler for {@link CustomerRestoredEvent}.
     * Marks the customer aggregate as active again by setting the {@code isDeleted} flag to false.
     *
     * @param event the event containing customer restoration information
     * @throws NullPointerException if {@code event} is null
     */
    @EventSourcingHandler
    public void on(@NotNull CustomerRestoredEvent event) {
        log.debug("Applying CustomerRestoredEvent for customer id: {}", event.getId());
        this.customerId = event.getId();
        this.auditAuthor = event.getAuthor();
        this.auditDateTime = event.getDateTime();
        this.isDeleted = Boolean.FALSE;
        log.info("Customer restored with id: {}, author: {}", this.customerId, this.auditAuthor);
    }
}