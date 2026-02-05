package com.mounanga.customercommandservice.validation;

import com.mounanga.customercommandservice.event.CustomerCreatedEvent;
import com.mounanga.customercommandservice.event.CustomerDeletedEvent;
import com.mounanga.customercommandservice.event.CustomerRestoredEvent;
import com.mounanga.customercommandservice.event.CustomerUpdatedEvent;
import com.mounanga.customercommandservice.exception.ResourceNotFoundException;
import com.mounanga.customercommandservice.publisher.CustomerEventPublisher;
import com.mounanga.customercommandservice.validation.db.CustomerFieldValidator;
import com.mounanga.customercommandservice.validation.db.CustomerFieldValidatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * Event handler component for processing customer-related events and maintaining
 * customer field validation data. This component listens to customer events and
 * updates the validation repository to ensure data consistency and integrity.
 *
 * <p>The handler is part of a separate processing group to enable independent
 * scaling and processing of validation-related events. It maintains the
 * {@link CustomerFieldValidator} entities which are used for checking uniqueness
 * constraints (email, NIP) and tracking deletion status.</p>
 *
 * <p>In addition to updating the validation repository, this handler also publishes
 * events to external systems via the {@link CustomerEventPublisher}, enabling
 * event-driven integration with other services.</p>
 *
 * <p>This component implements the Event Sourcing pattern where events are
 * the source of truth, and this handler projects those events into a query
 * model optimized for validation purposes while also propagating events
 * for downstream consumption.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Component
@ProcessingGroup("customerEmailNip")
@Slf4j
public class CustomerEventHandler {

    /**
     * Publisher for forwarding customer events to external systems or messaging infrastructure.
     */
    private final CustomerEventPublisher publisher;

    /**
     * Constructs a new CustomerEventHandler with the specified event publisher.
     *
     * @param publisher the publisher used to forward customer events to external systems
     */
    public CustomerEventHandler(CustomerEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Handles {@link CustomerCreatedEvent} by creating a new {@link CustomerFieldValidator}
     * entity in the repository and publishing the event externally.
     *
     * <p>This ensures that the new customer's email and NIP are registered for
     * uniqueness validation in future operations and that the creation event is
     * propagated to other services.</p>
     *
     * @param event the customer created event containing customer data
     * @param repository the repository for persisting validation entities
     * @throws NullPointerException if {@code event} or {@code repository} is null
     */
    @EventHandler
    public void on(@NotNull CustomerCreatedEvent event, @NotNull CustomerFieldValidatorRepository repository) {
        log.debug("Handling CustomerCreatedEvent for customer id: {}", event.getId());

        CustomerFieldValidator validator = new CustomerFieldValidator(
                event.getId(),
                event.getNip(),
                event.getEmail(),
                Boolean.FALSE
        );
        repository.save(validator);

        log.info("CustomerFieldValidator created for customer id: {}, email: {}", event.getId(), event.getEmail());
        publisher.publish(event);
    }

    /**
     * Handles {@link CustomerUpdatedEvent} by updating the existing {@link CustomerFieldValidator}
     * entity with new email and NIP values and publishing the event externally.
     *
     * <p>This ensures that uniqueness constraints are maintained when customer
     * information changes and that update events are propagated to other services.</p>
     *
     * @param event the customer updated event containing updated customer data
     * @param repository the repository for accessing and updating validation entities
     * @throws ResourceNotFoundException if no validation entity exists for the customer ID
     * @throws NullPointerException if {@code event} or {@code repository} is null
     */
    @EventHandler
    public void on(@NotNull CustomerUpdatedEvent event, @NotNull CustomerFieldValidatorRepository repository) {
        log.debug("Handling CustomerUpdatedEvent for customer id: {}", event.getId());

        CustomerFieldValidator validator = repository.findById(event.getId()).orElseThrow(
                () -> {
                    log.error("Customer not found with id: {} during update event handling", event.getId());
                    return new ResourceNotFoundException("Customer not found with id : " + event.getId());
                }
        );

        validator.setNip(event.getNip());
        validator.setEmail(event.getEmail());
        validator.setIsDeleted(Boolean.FALSE);
        repository.save(validator);

        log.info("CustomerFieldValidator updated for customer id: {}, new email: {}", event.getId(), event.getEmail());
        publisher.publish(event);
    }

    /**
     * Handles {@link CustomerDeletedEvent} by marking the {@link CustomerFieldValidator}
     * entity as deleted and publishing the event externally.
     *
     * <p>This implements soft delete functionality while maintaining the historical
     * record of email and NIP assignments for audit purposes and ensures deletion
     * events are propagated to other services.</p>
     *
     * @param event the customer deleted event containing deletion information
     * @param repository the repository for accessing and updating validation entities
     * @throws ResourceNotFoundException if no validation entity exists for the customer ID
     * @throws NullPointerException if {@code event} or {@code repository} is null
     */
    @EventHandler
    public void on(@NotNull CustomerDeletedEvent event, @NotNull CustomerFieldValidatorRepository repository) {
        log.debug("Handling CustomerDeletedEvent for customer id: {}", event.getId());

        CustomerFieldValidator validator = repository.findById(event.getId()).orElseThrow(
                () -> {
                    log.error("Customer not found with id: {} during delete event handling", event.getId());
                    return new ResourceNotFoundException("Customer not found with id: " + event.getId());
                }
        );

        validator.setIsDeleted(Boolean.TRUE);
        repository.save(validator);

        log.info("CustomerFieldValidator marked as deleted for customer id: {}", event.getId());
        publisher.publish(event);
    }

    /**
     * Handles {@link CustomerRestoredEvent} by marking the {@link CustomerFieldValidator}
     * entity as active (not deleted) and publishing the event externally.
     *
     * <p>This allows for restoration of previously deleted customers while maintaining
     * data integrity and ensuring restoration events are propagated to other services.</p>
     *
     * @param event the customer restored event containing restoration information
     * @param repository the repository for accessing and updating validation entities
     * @throws ResourceNotFoundException if no validation entity exists for the customer ID
     * @throws NullPointerException if {@code event} or {@code repository} is null
     */
    @EventHandler
    public void on(@NotNull CustomerRestoredEvent event, @NotNull CustomerFieldValidatorRepository repository) {
        log.debug("Handling CustomerRestoredEvent for customer id: {}", event.getId());

        CustomerFieldValidator validator = repository.findById(event.getId()).orElseThrow(
                () -> {
                    log.error("Customer not found with id: {} during restore event handling", event.getId());
                    return new ResourceNotFoundException("Customer not found with id: " + event.getId());
                }
        );

        validator.setIsDeleted(Boolean.FALSE);
        repository.save(validator);

        log.info("CustomerFieldValidator marked as active (restored) for customer id: {}", event.getId());
        publisher.publish(event);
    }
}
