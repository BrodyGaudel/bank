package com.mounanga.accountcommandservice.publisher;


import com.mounanga.accountcommandservice.event.BaseEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Service interface for publishing account events to Kafka.
 * This service is responsible for sending account-related events to the appropriate
 * Kafka topics for consumption by the account-query-service.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
public interface AccountEventPublisher {

    /**
     * Publishes a account event to the appropriate Kafka topic based on the event type.
     * This method is executed asynchronously to avoid blocking the command handling thread.
     *
     * @param baseEvent the account event to publish (accountCreatedEvent, accountUpdatedEvent,
     *                  accountDeletedEvent, or accountRestoredEvent)
     * @throws IllegalArgumentException if the event type is not recognized
     */
    void publish(@NotNull BaseEvent<String> baseEvent);
}
