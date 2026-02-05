package com.mounanga.customercommandservice.util;

import com.mounanga.customercommandservice.command.CreateCustomerCommand;
import com.mounanga.customercommandservice.command.DeleteCustomerCommand;
import com.mounanga.customercommandservice.command.RestoreCustomerCommand;
import com.mounanga.customercommandservice.command.UpdateCustomerCommand;
import com.mounanga.customercommandservice.event.CustomerCreatedEvent;
import com.mounanga.customercommandservice.event.CustomerDeletedEvent;
import com.mounanga.customercommandservice.event.CustomerRestoredEvent;
import com.mounanga.customercommandservice.event.CustomerUpdatedEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Factory class for creating event objects from corresponding commands in the Customer Command Service.
 * This utility class follows the Event Sourcing pattern by transforming command objects into
 * event objects that represent state changes in the system.
 *
 * <p>Each method in this factory creates an immutable event object that captures
 * the intent of a command and will be persisted to the event store.</p>
 *
 * <p>This class ensures that events maintain the same data integrity and business rules
 * as their originating commands while providing a clear audit trail of all changes.</p>
 *
 * <p>This class is not meant to be instantiated as it only contains static methods.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
public class EventFactory {

    /**
     * Private constructor to prevent instantiation.
     * This class should be used via its static methods only.
     */
    private EventFactory(){
        super();
    }

    /**
     * Creates a {@link CustomerCreatedEvent} from a {@link CreateCustomerCommand}.
     * This event represents the initial creation of a customer in the system.
     *
     * @param command the command object containing customer creation data
     * @return a new {@link CustomerCreatedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull CustomerCreatedEvent from(final @NotNull CreateCustomerCommand command){
        return new CustomerCreatedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor(),
                command.getFirstname(),
                command.getLastname(),
                command.getPlaceOfBirth(),
                command.getDateOfBirth(),
                command.getGender(),
                command.getNip(),
                command.getEmail(),
                command.getNationality()
        );
    }

    /**
     * Creates a {@link CustomerUpdatedEvent} from an {@link UpdateCustomerCommand}.
     * This event represents a modification to an existing customer's information.
     *
     * @param command the command object containing updated customer data
     * @return a new {@link CustomerUpdatedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull CustomerUpdatedEvent from(final @NotNull UpdateCustomerCommand command){
        return new CustomerUpdatedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor(),
                command.getFirstname(),
                command.getLastname(),
                command.getPlaceOfBirth(),
                command.getDateOfBirth(),
                command.getGender(),
                command.getNip(),
                command.getEmail(),
                command.getNationality()
        );
    }

    /**
     * Creates a {@link CustomerRestoredEvent} from a {@link RestoreCustomerCommand}.
     * This event represents the logical restoration of a customer from the system.
     *
     * @param command the command object containing customer restoration information
     * @return a new {@link CustomerRestoredEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull CustomerRestoredEvent from(final @NotNull RestoreCustomerCommand command){
        return new CustomerRestoredEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor()
        );
    }

    /**
     * Creates a {@link CustomerDeletedEvent} from a {@link DeleteCustomerCommand}.
     * This event represents the logical deletion of a customer from the system.
     *
     * @param command the command object containing customer deletion information
     * @return a new {@link CustomerDeletedEvent} populated with data from the command
     * @throws NullPointerException if {@code command} is null
     */
    @Contract("_ -> new")
    public static @NotNull CustomerDeletedEvent from(final @NotNull DeleteCustomerCommand command){
        return new CustomerDeletedEvent(
                command.getId(),
                command.getDateTime(),
                command.getAuthor()
        );
    }
}