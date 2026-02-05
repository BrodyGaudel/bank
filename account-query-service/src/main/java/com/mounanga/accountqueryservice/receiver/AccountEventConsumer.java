package com.mounanga.accountqueryservice.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mounanga.accountqueryservice.event.*;
import ga.aninf.accountqueryservice.event.*;
import com.mounanga.accountqueryservice.service.AccountEventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for accounts events.
 * This component listens to accounts event topics and delegates event processing
 * to the AccountEventHandlerService.
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
public class AccountEventConsumer {

    private static final String ACCOUNT_CREATED_TOPIC = "AccountCreatedEvent";
    private static final String ACCOUNT_CREDITED_TOPIC = "AccountCreditedEvent";
    private static final String ACCOUNT_DEBITED_TOPIC = "AccountDebitedEvent";
    private static final String ACCOUNT_ACTIVATED_TOPIC = "AccountActivatedEvent";
    private static final String ACCOUNT_DEACTIVATED_TOPIC = "AccountDeactivatedEvent";
    private static final String GROUP_ID = "account-query-group";

    private final ObjectMapper objectMapper;
    private final AccountEventHandlerService eventHandlerService;

    public AccountEventConsumer(ObjectMapper objectMapper, AccountEventHandlerService eventHandlerService) {
        this.objectMapper = objectMapper;
        this.eventHandlerService = eventHandlerService;
    }


   
    @KafkaListener(topics = ACCOUNT_CREATED_TOPIC, groupId = GROUP_ID)
    public void consumeAccountCreatedEvent(String jsonEvent) {
        log.debug("Consuming message from topic: {}.", ACCOUNT_CREATED_TOPIC);
        try {
            AccountCreatedEvent event = objectMapper.readValue(jsonEvent, AccountCreatedEvent.class);
            log.info("Received AccountCreatedEvent: id={},", event.getId());
            eventHandlerService.handle(event);
            log.debug("AccountCreatedEvent processed successfully for Account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process AccountCreatedEvent from topic {}: error={}", ACCOUNT_CREATED_TOPIC, e.getMessage(), e);
        }
    }


    @KafkaListener(topics = ACCOUNT_ACTIVATED_TOPIC, groupId = GROUP_ID)
    public void consumeAccountActivateEvent(String jsonEvent) {
        log.debug("Consuming message from topic: {}. ", ACCOUNT_ACTIVATED_TOPIC);
        try {
            AccountActivatedEvent event = objectMapper.readValue(jsonEvent, AccountActivatedEvent.class);
            log.info("Received AccountActivatedEvent: id={}", event.getId());
            eventHandlerService.handle(event);
            log.debug("AccountActivatedEvent processed successfully for Account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process AccountActivatedEvent from topic {}: error={}", ACCOUNT_ACTIVATED_TOPIC, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = ACCOUNT_ACTIVATED_TOPIC, groupId = GROUP_ID)
    public void consumeAccountDeactivateEvent(String jsonEvent) {
        log.debug("Consuming message from topic: {} ", ACCOUNT_DEACTIVATED_TOPIC);
        try {
            AccountDeactivatedEvent event = objectMapper.readValue(jsonEvent, AccountDeactivatedEvent.class);
            log.info("Received AccountDeactivatedEvent: id={}", event.getId());
            eventHandlerService.handle(event);
            log.debug("AccountDeactivatedEvent processed successfully for Account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process AccountDeactivatedEvent from topic {}: error={}", ACCOUNT_DEACTIVATED_TOPIC, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = ACCOUNT_CREDITED_TOPIC, groupId = GROUP_ID)
    public void consumeAccountCreditedEvent(String jsonEvent) {
        log.debug("Consuming message from topic : {} . ", ACCOUNT_CREDITED_TOPIC);
        try {
            AccountCreditedEvent event = objectMapper.readValue(jsonEvent, AccountCreditedEvent.class);
            log.info("Received AccountCreditedEvent: id={}", event.getId());
            eventHandlerService.handle(event);
            log.debug("AccountCreditedEvent processed successfully for Account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process AccountCreditedEvent from topic {}: error={}", ACCOUNT_CREDITED_TOPIC, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = ACCOUNT_DEBITED_TOPIC, groupId = GROUP_ID)
    public void consumeAccountDebitedEvent(String jsonEvent) {
        log.debug("Consuming message from topic : {}.", ACCOUNT_DEBITED_TOPIC);
        try {
            AccountDebitedEvent event = objectMapper.readValue(jsonEvent, AccountDebitedEvent.class);
            log.info("Received AccountDebitedEvent: id={}", event.getId());
            eventHandlerService.handle(event);
            log.debug("AccountDebitedEvent processed successfully for Account id: {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process AccountDebitedEvent from topic {}: error={}", ACCOUNT_DEBITED_TOPIC, e.getMessage(), e);
        }
    }


}
