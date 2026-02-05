package com.mounanga.customercommandservice.web;

import com.mounanga.customercommandservice.command.CreateCustomerCommand;
import com.mounanga.customercommandservice.command.DeleteCustomerCommand;
import com.mounanga.customercommandservice.command.RestoreCustomerCommand;
import com.mounanga.customercommandservice.command.UpdateCustomerCommand;
import com.mounanga.customercommandservice.dto.CustomerRequestDTO;
import com.mounanga.customercommandservice.util.CommandFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for managing customer command operations.
 * Provides endpoints for creating, updating, and deleting customer records
 * using Axon Framework's command handling mechanism.
 *
 * <p>This controller follows CQRS principles where all write operations
 * are handled as commands that are dispatched through the Command Gateway.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Command API", description = "API endpoints for customer command operations (write operations)")
public class CustomerRestController {

    /**
     * The Axon Command Gateway for dispatching commands to the command bus.
     */
    private final CommandGateway commandGateway;

    /**
     * Constructs a new CustomerRestController with the specified CommandGateway.
     *
     * @param commandGateway the Axon Command Gateway used for dispatching commands
     */
    public CustomerRestController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    /**
     * Creates a new customer by dispatching a CreateCustomerCommand.
     *
     * @param dto the customer data transfer object containing customer information
     * @return a CompletableFuture that completes with the unique identifier of the created customer
     */
    @PostMapping
    @Operation(
            summary = "Create a new customer",
            description = "Creates a new customer record by dispatching a CreateCustomerCommand. " +
                    "The command is processed asynchronously through Axon Framework's command bus."
    )
    public CompletableFuture<String> create(
            @Parameter(
                    description = "Customer data transfer object containing customer information",
                    required = true,
                    schema = @Schema(implementation = CustomerRequestDTO.class)
            )
            @RequestBody @Valid CustomerRequestDTO dto) {
        CreateCustomerCommand command = CommandFactory.buildCreate(dto);
        return commandGateway.send(command);
    }

    /**
     * Updates an existing customer by dispatching an UpdateCustomerCommand.
     *
     * @param id the unique identifier of the customer to update
     * @param dto the customer data transfer object containing updated customer information
     * @return a CompletableFuture that completes with the customer identifier
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing customer",
            description = "Updates an existing customer record by dispatching an UpdateCustomerCommand. " +
                    "The command is processed asynchronously through Axon Framework's command bus."
    )
    public CompletableFuture<String> update(
            @Parameter(
                    description = "UUID of the customer to update",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id,

            @Parameter(
                    description = "Customer data transfer object containing updated customer information",
                    required = true,
                    schema = @Schema(implementation = CustomerRequestDTO.class)
            )
            @RequestBody @Valid CustomerRequestDTO dto) {
        UpdateCustomerCommand command = CommandFactory.buildUpdate(id, dto);
        return commandGateway.send(command);
    }

    /**
     * Restores a customer by dispatching a RestoreCustomerCommand.
     *
     * @param id the unique identifier of the customer to restore
     * @return a CompletableFuture that completes with the customer identifier
     */
    @PatchMapping("/{id}")
    @Operation(
            summary = "Restore a customer",
            description = "Restores a customer record by dispatching a RestoreCustomerCommand. " +
                    "The command is processed asynchronously through Axon Framework's command bus."
    )
    public CompletableFuture<String> restore(
            @Parameter(
                    description = "UUID of the customer to restore",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        RestoreCustomerCommand command = CommandFactory.buildRestore(id);
        return commandGateway.send(command);
    }

    /**
     * Deletes a customer by dispatching a DeleteCustomerCommand.
     *
     * @param id the unique identifier of the customer to delete
     * @return a CompletableFuture that completes with the customer identifier
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a customer",
            description = "Deletes a customer record by dispatching a DeleteCustomerCommand. " +
                    "The command is processed asynchronously through Axon Framework's command bus."
    )
    public CompletableFuture<String> delete(
            @Parameter(
                    description = "UUID of the customer to delete",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable UUID id) {
        DeleteCustomerCommand command = CommandFactory.buildDelete(id);
        return commandGateway.send(command);
    }

}
