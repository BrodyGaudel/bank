package com.mounanga.customercommandservice.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mounanga.customercommandservice.event.BaseEvent;
import com.mounanga.customercommandservice.event.CustomerCreatedEvent;
import com.mounanga.customercommandservice.event.CustomerDeletedEvent;
import com.mounanga.customercommandservice.event.CustomerRestoredEvent;
import com.mounanga.customercommandservice.event.CustomerUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Implementation of CustomerEventPublisher that sends events as JSON strings to Kafka topics.
 * This approach avoids the deprecated JsonSerializer by manually converting events to JSON.
 *
 * <p>Each event type is sent to its dedicated topic for better separation of concerns.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class CustomerEventPublisherImpl implements CustomerEventPublisher {

    // Topic names as constants
    private static final String CUSTOMER_CREATED_TOPIC = "CustomerCreatedEvent";
    private static final String CUSTOMER_UPDATED_TOPIC = "CustomerUpdatedEvent";
    private static final String CUSTOMER_DELETED_TOPIC = "CustomerDeletedEvent";
    private static final String CUSTOMER_RESTORED_TOPIC = "CustomerRestoredEvent";

    private final KafkaTemplate<@NotNull String, @NotNull String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    /**
     * Constructs a new CustomerEventPublisherImpl.
     *
     * @param kafkaTemplate the Kafka template used to send messages
     */
    public CustomerEventPublisherImpl(KafkaTemplate<@NotNull String, @NotNull String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Publishes a customer event to the appropriate Kafka topic asynchronously.
     * The event is serialized to JSON string before sending.
     *
     * @param baseEvent the customer event to publish
     * @throws IllegalArgumentException if the event type is not recognized
     * @throws RuntimeException if JSON serialization fails
     */
    @Async
    @Override
    public void publish(@NotNull BaseEvent<String> baseEvent) {
        log.debug("Publishing event to Kafka: event type={}, id={}", baseEvent.getClass().getSimpleName(), baseEvent.getId());

        try {
            String jsonEvent = objectMapper.writeValueAsString(baseEvent);
            String topic = determineTopicForEvent(baseEvent);

            CompletableFuture<SendResult<@NotNull String, @NotNull String>> future =
                    kafkaTemplate.send(topic, baseEvent.getId(), jsonEvent);

            handleKafkaResponse(future, baseEvent, topic);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event to JSON: event type={}, id={}, error={}",
                    baseEvent.getClass().getSimpleName(),
                    baseEvent.getId(),
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to serialize event to JSON", e);
        }
    }

    /**
     * Determines the appropriate Kafka topic based on the event type.
     *
     * @param baseEvent the event to be published
     * @return the topic name
     * @throws IllegalArgumentException if the event type is not recognized
     */
    private String determineTopicForEvent(@NotNull BaseEvent<String> baseEvent) {

        switch (baseEvent) {
            case CustomerCreatedEvent ignored -> {
                return CUSTOMER_CREATED_TOPIC;
            }
            case CustomerDeletedEvent ignored -> {
                return CUSTOMER_DELETED_TOPIC;
            }
            case CustomerUpdatedEvent ignored -> {
                return CUSTOMER_UPDATED_TOPIC;
            }
            case CustomerRestoredEvent ignored -> {
                return CUSTOMER_RESTORED_TOPIC;
            }
            default -> {
                log.error("Unknown event type: {}", baseEvent.getClass().getSimpleName());
                throw new IllegalArgumentException("Unknown event type: " + baseEvent.getClass().getSimpleName());
            }
        }
    }

    /**
     * Handles the asynchronous response from Kafka after sending an event.
     *
     * @param future the CompletableFuture representing the send operation
     * @param event the event that was sent
     * @param topic the Kafka topic
     */
    private void handleKafkaResponse(@NotNull CompletableFuture<SendResult<@NotNull String, @NotNull String>> future, BaseEvent<String> event, String topic) {
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Event published successfully to Kafka: event type={}, id={}, topic={}, partition={}, offset={}",
                        event.getClass().getSimpleName(),
                        event.getId(),
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish event to Kafka: event type={}, id={}, topic={}, error={}",
                        event.getClass().getSimpleName(),
                        event.getId(),
                        topic,
                        ex.getMessage(),
                        ex);
            }
        });
    }
}