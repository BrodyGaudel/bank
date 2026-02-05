package com.mounanga.customercommandservice.validation;

import com.mounanga.customercommandservice.command.CreateCustomerCommand;
import com.mounanga.customercommandservice.command.DeleteCustomerCommand;
import com.mounanga.customercommandservice.command.RestoreCustomerCommand;
import com.mounanga.customercommandservice.command.UpdateCustomerCommand;
import com.mounanga.customercommandservice.exception.ResourceAlreadyExistsException;
import com.mounanga.customercommandservice.exception.ResourceNotFoundException;
import com.mounanga.customercommandservice.validation.db.CustomerFieldValidator;
import com.mounanga.customercommandservice.validation.db.CustomerFieldValidatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Interceptor for dispatching commands that performs pre-command validation.
 * This component intercepts commands before they are sent to the command bus and validates
 * business rules such as uniqueness constraints and existence checks.
 *
 * <p>The interceptor ensures data integrity by:
 * <ul>
 *   <li>Checking for duplicate NIP and email during customer creation</li>
 *   <li>Verifying customer existence before updates and deletions</li>
 *   <li>Preventing modifications to deleted customers</li>
 *   <li>Validating uniqueness constraints during updates</li>
 * </ul>
 *
 * <p>This interceptor implements the Axon Framework's {@link MessageDispatchInterceptor}
 * interface to intercept and validate commands before they reach the aggregate.</p>
 *
 * <p>Validation failures result in appropriate exceptions being thrown, which are
 * translated into meaningful error responses by the exception handling mechanism.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class AggregateCommandDispatchInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    /**
     * Repository for accessing customer field validation data.
     */
    private final CustomerFieldValidatorRepository repository;

    /**
     * Constructs a new AggregateCommandDispatchInterceptor with the specified repository.
     *
     * @param repository the repository used for customer field validation
     */
    public AggregateCommandDispatchInterceptor(CustomerFieldValidatorRepository repository) {
        this.repository = repository;
    }

    /**
     * Handles a single command message. This method is called for each command
     * during the dispatch process but delegates to the batch processing method.
     *
     * @param message the command message to handle
     * @return the processed command message
     */
    @NotNull
    @Override
    public CommandMessage<?> handle(@NotNull CommandMessage<?> message) {
        return MessageDispatchInterceptor.super.handle(message);
    }

    /**
     * Processes a batch of command messages and applies validation logic based on
     * the command type. This method is called by the Axon Framework when commands
     * are dispatched.
     *
     * @param messages the list of command messages to process
     * @return a function that processes individual command messages within the batch
     */
    @NotNull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@NotNull List<? extends CommandMessage<?>> messages) {
        return (i, m) -> {
            if (CreateCustomerCommand.class.equals(m.getPayloadType())) {
                validateCreateCustomerCommand(m);
            } else if (DeleteCustomerCommand.class.equals(m.getPayloadType())) {
                validateDeleteCustomerCommand(m);
            } else if (UpdateCustomerCommand.class.equals(m.getPayloadType())) {
                validateUpdateCustomerCommand(m);
            } else if (RestoreCustomerCommand.class.equals(m.getPayloadType())) {
                validateRestoreCustomerCommand(m);
            }
            return m;
        };
    }

    /**
     * Validates a {@link CreateCustomerCommand} by checking:
     * <ul>
     *   <li>Customer ID uniqueness</li>
     *   <li>Email uniqueness</li>
     *   <li>NIP uniqueness</li>
     * </ul>
     *
     * @param m the command message containing the CreateCustomerCommand
     * @throws ResourceAlreadyExistsException if any of the uniqueness constraints are violated
     */
    private void validateCreateCustomerCommand(@NotNull CommandMessage<?> m) {
        final CreateCustomerCommand command = (CreateCustomerCommand) m.getPayload();
        log.debug("Validating CreateCustomerCommand for customer id: {}", command.getId());

        if (repository.existsById(command.getId())) {
            log.warn("Validation failed: Customer with id {} already exists", command.getId());
            throw new ResourceAlreadyExistsException(String.format("Customer with customer's id %s already exists", command.getId()));
        }
        if (repository.existsByEmail(command.getEmail())) {
            log.warn("Validation failed: Customer with email {} already exists", command.getEmail());
            throw new ResourceAlreadyExistsException(String.format("Customer with customer's email %s already exists", command.getEmail()));
        }
        if (repository.existsByNip(command.getNip())) {
            log.warn("Validation failed: Customer with nip {} already exists", command.getNip());
            throw new ResourceAlreadyExistsException(String.format("Customer with customer's nip %s already exists", command.getNip()));
        }

        log.debug("CreateCustomerCommand validation passed for customer id: {}", command.getId());
    }

    /**
     * Validates an {@link UpdateCustomerCommand} by checking:
     * <ul>
     *   <li>Customer existence</li>
     *   <li>That the customer is not deleted</li>
     *   <li>Email uniqueness (if changed)</li>
     *   <li>NIP uniqueness (if changed)</li>
     * </ul>
     *
     * @param m the command message containing the UpdateCustomerCommand
     * @throws ResourceNotFoundException if the customer does not exist
     * @throws IllegalStateException if the customer is deleted
     * @throws ResourceAlreadyExistsException if email or NIP uniqueness constraints are violated
     */
    private void validateUpdateCustomerCommand(@NotNull CommandMessage<?> m) {
        final UpdateCustomerCommand command = (UpdateCustomerCommand) m.getPayload();
        log.debug("Validating UpdateCustomerCommand for customer id: {}", command.getId());

        CustomerFieldValidator validator = repository.findById(command.getId())
                .orElseThrow(() -> {
                    log.warn("Validation failed: Customer with id {} does not exist .", command.getId());
                    return new ResourceNotFoundException(String.format("Customer with customer's id %s does not exist", command.getId()));
                });

        if (Boolean.TRUE.equals(validator.getIsDeleted())) {
            log.warn("Validation failed: Cannot update deleted customer with id {}", command.getId());
            throw new IllegalStateException(String.format("Cannot update deleted customer with id %s", command.getId()));
        }

        if (!validator.getEmail().equals(command.getEmail()) && repository.existsByEmail(command.getEmail())) {
            log.warn("Validation failed: Email {} already exists for another customer", command.getEmail());
            throw new ResourceAlreadyExistsException(String.format("Customer with customer's email %s already exists", command.getEmail()));
        }
        if (!validator.getNip().equals(command.getNip()) && repository.existsByNip(command.getNip())) {
            log.warn("Validation failed: NIP {} already exists for another customer", command.getNip());
            throw new ResourceAlreadyExistsException(String.format("Customer with customer's nip %s already exists", command.getNip()));
        }

        log.debug("UpdateCustomerCommand validation passed for customer id: {}", command.getId());
    }

    /**
     * Validates a {@link DeleteCustomerCommand} by checking:
     * <ul>
     *   <li>Customer existence</li>
     *   <li>That the customer is not already deleted</li>
     * </ul>
     *
     * @param m the command message containing the DeleteCustomerCommand
     * @throws ResourceNotFoundException if the customer does not exist
     * @throws IllegalStateException if the customer is already deleted
     */
    private void validateDeleteCustomerCommand(@NotNull CommandMessage<?> m) {
        final DeleteCustomerCommand command = (DeleteCustomerCommand) m.getPayload();
        log.debug("Validating DeleteCustomerCommand for customer id: {}", command.getId());

        CustomerFieldValidator validator = repository.findById(command.getId())
                .orElseThrow(() -> {
                    log.warn("Validation failed: Customer with id {} does not exist", command.getId());
                    return new ResourceNotFoundException(String.format("Customer with id %s does not exist", command.getId()));
                });

        if (Boolean.TRUE.equals(validator.getIsDeleted())) {
            log.warn("Validation failed: Customer with id {} is already deleted", command.getId());
            throw new IllegalStateException(String.format("Customer with id %s is already deleted", command.getId()));
        }

        log.debug("DeleteCustomerCommand validation passed for customer id: {}", command.getId());
    }

    /**
     * Validates a {@link RestoreCustomerCommand} by checking:
     * <ul>
     *   <li>Customer existence</li>
     *   <li>That the customer is currently deleted (can be restored)</li>
     * </ul>
     *
     * @param m the command message containing the RestoreCustomerCommand
     * @throws ResourceNotFoundException if the customer does not exist
     * @throws IllegalStateException if the customer is already active (not deleted)
     */
    private void validateRestoreCustomerCommand(@NotNull CommandMessage<?> m) {
        final RestoreCustomerCommand command = (RestoreCustomerCommand) m.getPayload();
        log.debug("Validating RestoreCustomerCommand for customer id: {}", command.getId());

        CustomerFieldValidator validator = repository.findById(command.getId())
                .orElseThrow(() -> {
                    log.warn("Validation failed: Customer with id {} does not exist.", command.getId());
                    return new ResourceNotFoundException(String.format("Customer with id %s does not exist", command.getId()));
                });

        if (Boolean.FALSE.equals(validator.getIsDeleted())) {
            log.warn("Validation failed: Customer with id {} is already active (not deleted)", command.getId());
            throw new IllegalStateException(String.format("Customer with id %s is already active", command.getId()));
        }

        log.debug("RestoreCustomerCommand validation passed for customer id: {}", command.getId());
    }
}