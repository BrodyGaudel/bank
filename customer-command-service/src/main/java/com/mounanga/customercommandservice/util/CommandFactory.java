package com.mounanga.customercommandservice.util;

import com.mounanga.customercommandservice.command.CreateCustomerCommand;
import com.mounanga.customercommandservice.command.DeleteCustomerCommand;
import com.mounanga.customercommandservice.command.RestoreCustomerCommand;
import com.mounanga.customercommandservice.command.UpdateCustomerCommand;
import com.mounanga.customercommandservice.dto.CustomerRequestDTO;
import com.mounanga.customercommandservice.security.SecurityUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory class for creating command objects in the Customer Command Service.
 * This utility class provides static methods to construct various customer-related
 * commands with properly initialized metadata (ID, timestamp, current user).
 *
 * <p>The factory ensures consistent command creation by automatically setting:
 * <ul>
 *   <li>Unique identifiers for create commands</li>
 *   <li>Current timestamp for command execution</li>
 *   <li>Current authenticated user from security context</li>
 * </ul>
 *
 * <p>This class is not meant to be instantiated as it only contains static methods.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
public class CommandFactory {

    /**
     * Private constructor to prevent instantiation.
     * This class should be used via its static methods only.
     */
    private CommandFactory() {
        super();
    }

    /**
     * Creates a {@link CreateCustomerCommand} from the provided customer data.
     * Generates a unique ID, sets the current timestamp, and retrieves the current
     * authenticated user from the security context.
     *
     * @param dto the customer data transfer object containing customer information
     * @return a fully initialized {@link CreateCustomerCommand} ready for execution
     * @throws NullPointerException if {@code dto} is null
     */
    @Contract("_ -> new")
    public static @NotNull CreateCustomerCommand buildCreate(final @NotNull CustomerRequestDTO dto) {
        return new CreateCustomerCommand(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername(),
                dto.firstname(),
                dto.lastname(),
                dto.placeOfBirth(),
                dto.dateOfBirth(),
                dto.gender(),
                dto.nip(),
                dto.email(),
                dto.nationality()
        );
    }

    /**
     * Creates an {@link UpdateCustomerCommand} for an existing customer.
     * Sets the current timestamp and retrieves the current authenticated user
     * from the security context.
     *
     * @param id the unique identifier of the customer to update
     * @param dto the customer data transfer object containing updated customer information
     * @return a fully initialized {@link UpdateCustomerCommand} ready for execution
     * @throws NullPointerException if {@code id} or {@code dto} is null
     */
    @Contract("_, _ -> new")
    public static @NotNull UpdateCustomerCommand buildUpdate(@NotNull UUID id, final @NotNull CustomerRequestDTO dto) {
        return new UpdateCustomerCommand(
                id.toString(),
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername(),
                dto.firstname(),
                dto.lastname(),
                dto.placeOfBirth(),
                dto.dateOfBirth(),
                dto.gender(),
                dto.nip(),
                dto.email(),
                dto.nationality()
        );
    }

    /**
     * Creates a {@link DeleteCustomerCommand} for removing a customer.
     * Sets the current timestamp and retrieves the current authenticated user
     * from the security context.
     *
     * @param id the unique identifier of the customer to delete
     * @return a fully initialized {@link DeleteCustomerCommand} ready for execution
     * @throws NullPointerException if {@code id} is null
     */
    @Contract("_ -> new")
    public static @NotNull DeleteCustomerCommand buildDelete(final @NotNull UUID id) {
        return new DeleteCustomerCommand(
                id.toString(),
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername()
        );
    }

    /**
     * Creates a {@link RestoreCustomerCommand} for restore a customer.
     * Sets the current timestamp and retrieves the current authenticated user
     * from the security context.
     *
     * @param id the unique identifier of the customer to restore
     * @return a fully initialized {@link RestoreCustomerCommand} ready for execution
     * @throws NullPointerException if {@code id} is null
     */
    @Contract("_ -> new")
    public static @NotNull RestoreCustomerCommand buildRestore(final @NotNull UUID id) {
        return new RestoreCustomerCommand(
                id.toString(),
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername()
        );
    }
}