package com.mounanga.customercommandservice.publisher;

import com.mounanga.customercommandservice.event.BaseEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Service interface for publishing customer events to Kafka.
 * This service is responsible for sending customer-related events to the appropriate
 * Kafka topics for consumption by the customer-query-service.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
public interface CustomerEventPublisher {

    /**
     * Publishes a customer event to the appropriate Kafka topic based on the event type.
     * This method is executed asynchronously to avoid blocking the command handling thread.
     *
     * @param baseEvent the customer event to publish (CustomerCreatedEvent, CustomerUpdatedEvent,
     *                  CustomerDeletedEvent, or CustomerRestoredEvent)
     * @throws IllegalArgumentException if the event type is not recognized
     */
    void publish(@NotNull BaseEvent<String> baseEvent);
}
