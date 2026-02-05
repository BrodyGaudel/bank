package com.mounanga.customerqueryservice.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mounanga.customerqueryservice.event.CustomerCreatedEvent;
import com.mounanga.customerqueryservice.event.CustomerDeletedEvent;
import com.mounanga.customerqueryservice.event.CustomerRestoredEvent;
import com.mounanga.customerqueryservice.event.CustomerUpdatedEvent;
import com.mounanga.customerqueryservice.service.CustomerEventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for customer events.
 * This component listens to customer event topics and delegates event processing
 * to the CustomerEventHandlerService.
 *
 * <p>Each event type has its dedicated listener method that:
 * <ul>
 *   <li>Deserializes the JSON event string to the appropriate event object</li>
 *   <li>Logs the reception of the event</li>
 *   <li>Delegates processing to the handler service</li>
 *   <li>Handles deserialization errors gracefully</li>
 * </ul>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class CustomerEventConsumer {

    private static final String CUSTOMER_CREATED_TOPIC = "CustomerCreatedEvent";
    private static final String CUSTOMER_UPDATED_TOPIC = "CustomerUpdatedEvent";
    private static final String CUSTOMER_DELETED_TOPIC = "CustomerDeletedEvent";
    private static final String CUSTOMER_RESTORED_TOPIC = "CustomerRestoredEvent";
    private static final String GROUP_ID = "customer-query-group";

    private final ObjectMapper objectMapper;
    private final CustomerEventHandlerService customerEventHandlerService;

    /**
     * Constructs a new CustomerEventConsumer.
     *
     * @param objectMapper the Jackson ObjectMapper for JSON deserialization
     * @param customerEventHandlerService the service that processes customer events
     */
    public CustomerEventConsumer(ObjectMapper objectMapper, CustomerEventHandlerService customerEventHandlerService) {
        this.objectMapper = objectMapper;
        this.customerEventHandlerService = customerEventHandlerService;
    }

    /**
     * Consumes CustomerCreatedEvent from Kafka topic.
     * Deserializes the JSON event and delegates processing to the handler service.
     *
     * @param jsonEvent the JSON string representation of the CustomerCreatedEvent
     */
    @KafkaListener(topics = CUSTOMER_CREATED_TOPIC, groupId = GROUP_ID)
    public void consumeCustomerCreatedEvent(String jsonEvent) {
        log.debug("Consuming message from topic: {}.", CUSTOMER_CREATED_TOPIC);
        try {
            CustomerCreatedEvent event = objectMapper.readValue(jsonEvent, CustomerCreatedEvent.class);
            log.info("Received CustomerCreatedEvent: id={}, email={}", event.getId(), event.getEmail());
            customerEventHandlerService.handle(event);
            log.debug("CustomerCreatedEvent processed successfully for customer id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process CustomerCreatedEvent from topic {}: error={}", CUSTOMER_CREATED_TOPIC, e.getMessage(), e);
        }
    }

    /**
     * Consumes CustomerUpdatedEvent from Kafka topic.
     * Deserializes the JSON event and delegates processing to the handler service.
     *
     * @param jsonEvent the JSON string representation of the CustomerUpdatedEvent
     */
    @KafkaListener(topics = CUSTOMER_UPDATED_TOPIC, groupId = GROUP_ID)
    public void consumeCustomerUpdatedEvent(String jsonEvent) {
        log.debug("Consuming message from topic: {}. ", CUSTOMER_UPDATED_TOPIC);
        try {
            CustomerUpdatedEvent event = objectMapper.readValue(jsonEvent, CustomerUpdatedEvent.class);
            log.info("Received CustomerUpdatedEvent: id={}, email={}", event.getId(), event.getEmail());
            customerEventHandlerService.handle(event);
            log.debug("CustomerUpdatedEvent processed successfully for customer id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process CustomerUpdatedEvent from topic {}: error={}",
                    CUSTOMER_UPDATED_TOPIC, e.getMessage(), e);
        }
    }

    /**
     * Consumes CustomerDeletedEvent from Kafka topic.
     * Deserializes the JSON event and delegates processing to the handler service.
     *
     * @param jsonEvent the JSON string representation of the CustomerDeletedEvent
     */
    @KafkaListener(topics = CUSTOMER_DELETED_TOPIC, groupId = GROUP_ID)
    public void consumeCustomerDeletedEvent(String jsonEvent) {
        log.debug("Consuming message from topic: {}", CUSTOMER_DELETED_TOPIC);
        try {
            CustomerDeletedEvent event = objectMapper.readValue(jsonEvent, CustomerDeletedEvent.class);
            log.info("Received CustomerDeletedEvent: id={}", event.getId());
            customerEventHandlerService.handle(event);
            log.debug("CustomerDeletedEvent processed successfully for customer id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process CustomerDeletedEvent from topic {}: error={}",
                    CUSTOMER_DELETED_TOPIC, e.getMessage(), e);
        }
    }

    /**
     * Consumes CustomerRestoredEvent from Kafka topic.
     * Deserializes the JSON event and delegates processing to the handler service.
     *
     * @param jsonEvent the JSON string representation of the CustomerRestoredEvent
     */
    @KafkaListener(topics = CUSTOMER_RESTORED_TOPIC, groupId = GROUP_ID)
    public void consumeCustomerRestoredEvent(String jsonEvent) {
        log.debug("Consuming message from topic: {}", CUSTOMER_RESTORED_TOPIC);
        try {
            CustomerRestoredEvent event = objectMapper.readValue(jsonEvent, CustomerRestoredEvent.class);
            log.info("Received CustomerRestoredEvent: id={}", event.getId());
            customerEventHandlerService.handle(event);
            log.debug("CustomerRestoredEvent processed successfully for customer id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process CustomerRestoredEvent from topic {}: error={}",
                    CUSTOMER_RESTORED_TOPIC, e.getMessage(), e);
        }
    }
}
