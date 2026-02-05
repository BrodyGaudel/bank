package com.mounanga.accountcommandservice.event;



import com.mounanga.accountcommandservice.publisher.AccountEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling account events and publishing them to Kafka.
 * This service acts as a bridge between Axon's event store and Kafka topics,
 * ensuring that all successfully processed events are propagated to the query service.
 *
 * <p>Each event type is handled individually and published to its corresponding
 * Kafka topic for consumption by the account-query-service.</p>
 *
 * <p>This handler is part of the "accountEventPublisher" processing group,
 * which uses tracking mode to ensure reliable event processing and publication.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Service
@ProcessingGroup("accountEventPublisher")
@Slf4j
public class EventHandlerService {

    private final AccountEventPublisher publisher;

    /**
     * Constructs a new EventHandlerService with the specified publisher.
     *
     * @param publisher the Kafka publisher for account events
     */
    public EventHandlerService(AccountEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Handles AccountCreatedEvent by publishing it to Kafka.
     * This event is triggered when a new account is successfully created.
     *
     * @param event the AccountCreatedEvent to handle
     */
    @EventHandler
    public void handle(@NotNull AccountCreatedEvent event) {
        log.debug("Handling AccountCreatedEvent for account id: {}", event.getId());
        try {
            publisher.publish(event);
            log.info("AccountCreatedEvent published to Kafka for account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to publish AccountCreatedEvent for account id: {}, error: {}", event.getId(), e.getMessage(), e);
            throw e; // Re-throw to trigger Axon retry mechanism
        }
    }

    /**
     * Handles AccountActivatedEvent by publishing it to Kafka.
     * This event is triggered when an account is activated.
     *
     * @param event the AccountActivatedEvent to handle
     */
    @EventHandler
    public void handle(@NotNull AccountActivatedEvent event) {
        log.debug("Handling AccountActivatedEvent for account id: {}", event.getId());
        try {
            publisher.publish(event);
            log.info("AccountActivatedEvent published to Kafka for account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to publish AccountActivatedEvent for account id: {}, error: {}", event.getId(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Handles AccountDeactivatedEvent by publishing it to Kafka.
     * This event is triggered when an account is deactivated.
     *
     * @param event the AccountDeactivatedEvent to handle
     */
    @EventHandler
    public void handle(@NotNull AccountDeactivatedEvent event) {
        log.debug("Handling AccountDeactivatedEvent for account id: {}", event.getId());
        try {
            publisher.publish(event);
            log.info("AccountDeactivatedEvent published to Kafka for account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to publish AccountDeactivatedEvent for account id: {}, error: {}", event.getId(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Handles AccountCreditedEvent by publishing it to Kafka.
     * This event is triggered when an account is credited with funds.
     *
     * @param event the AccountCreditedEvent to handle
     */
    @EventHandler
    public void handle(@NotNull AccountCreditedEvent event) {
        log.debug("Handling AccountCreditedEvent for account id: {}, amount: {}",
                event.getId(), event.getAmount());
        try {
            publisher.publish(event);
            log.info("AccountCreditedEvent published to Kafka for account id: {}, amount: {}", event.getId(), event.getAmount());
        } catch (Exception e) {
            log.error("Failed to publish AccountCreditedEvent for account id: {}, amount: {}, error: {}", event.getId(), event.getAmount(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Handles AccountDebitedEvent by publishing it to Kafka.
     * This event is triggered when an account is debited (funds withdrawn).
     *
     * @param event the AccountDebitedEvent to handle
     */
    @EventHandler
    public void handle(@NotNull AccountDebitedEvent event) {
        log.debug("Handling AccountDebitedEvent for account id: {}, amount: {}",
                event.getId(), event.getAmount());
        try {
            publisher.publish(event);
            log.info("AccountDebitedEvent published to Kafka for account id: {}, amount: {}",
                    event.getId(), event.getAmount());
        } catch (Exception e) {
            log.error("Failed to publish AccountDebitedEvent for account id: {}, amount: {}, error: {}",
                    event.getId(), event.getAmount(), e.getMessage(), e);
            throw e;
        }
    }
}
