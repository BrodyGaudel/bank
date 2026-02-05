package com.mounanga.accountcommandservice.util;

import com.mounanga.accountcommandservice.command.*;
import com.mounanga.accountcommandservice.dto.AccountRequestDTO;
import com.mounanga.accountcommandservice.dto.OperationDTO;
import com.mounanga.accountcommandservice.enums.AccountStatus;
import com.mounanga.accountcommandservice.security.SecurityUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Factory class for creating account command objects.
 * This utility class provides static methods to construct various account-related commands
 * with properly initialized metadata (RIB, timestamp, current user, etc.).
 *
 * <p>The factory ensures consistent command creation by automatically setting:
 * <ul>
 *   <li>Current timestamp for command execution</li>
 *   <li>Current authenticated user from security context</li>
 *   <li>Appropriate command parameters from provided DTOs</li>
 * </ul>
 *
 * <p>This class supports the creation of commands for the full account lifecycle:
 * <ul>
 *   <li>Account creation with initial state</li>
 *   <li>Credit and debit operations</li>
 *   <li>Account status changes (activation/deactivation)</li>
 * </ul>
 *
 * <p>This class is not meant to be instantiated as it only contains static methods.</p>
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
     * Creates a {@link CreateAccountCommand} from the provided RIB and account data.
     * Initializes the account with zero balance and status set to CREATED.
     *
     * @param rib the RIB (Relevé d'Identité Bancaire) for the new account
     * @param dto the account data transfer object containing account information
     * @return a fully initialized {@link CreateAccountCommand} ready for execution
     * @throws NullPointerException if {@code rib} or {@code dto} is null
     */
    public static @NotNull CreateAccountCommand buildCreate(@NotNull String rib, @NotNull AccountRequestDTO dto) {
        return new CreateAccountCommand(
                rib,
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername(),
                AccountStatus.CREATED,
                BigDecimal.ZERO,
                dto.getCurrency(),
                dto.getCustomerId().toString()
        );
    }

    /**
     * Creates a {@link CreditAccountCommand} from the provided operation data.
     * The command represents a credit operation that increases the account balance.
     *
     * @param dto the operation data transfer object containing credit operation details
     * @return a fully initialized {@link CreditAccountCommand} ready for execution
     * @throws NullPointerException if {@code dto} is null
     */
    @Contract("_ -> new")
    public static @NotNull CreditAccountCommand buildCredit(final @NotNull OperationDTO dto) {
        return new CreditAccountCommand(
                dto.getRib(),
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername(),
                dto.getAmount(),
                dto.getDescription(),
                dto.getCurrency()
        );
    }

    /**
     * Creates a {@link DebitAccountCommand} from the provided operation data.
     * The command represents a debit operation that decreases the account balance.
     *
     * @param dto the operation data transfer object containing debit operation details
     * @return a fully initialized {@link DebitAccountCommand} ready for execution
     * @throws NullPointerException if {@code dto} is null
     */
    @Contract("_ -> new")
    public static @NotNull DebitAccountCommand buildDebit(final @NotNull OperationDTO dto) {
        return new DebitAccountCommand(
                dto.getRib(),
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername(),
                dto.getAmount(),
                dto.getDescription(),
                dto.getCurrency()
        );
    }

    /**
     * Creates an {@link ActivateAccountCommand} for activating an account.
     *
     * @param rib the RIB of the account to activate
     * @return a fully initialized {@link ActivateAccountCommand} ready for execution
     * @throws NullPointerException if {@code rib} is null
     */
    @Contract("_ -> new")
    public static @NotNull ActivateAccountCommand buildActivate(final @NotNull String rib) {
        return new ActivateAccountCommand(
                rib,
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername()
        );
    }

    /**
     * Creates a {@link DeactivateAccountCommand} for deactivating an account.
     *
     * @param rib the RIB of the account to deactivate
     * @return a fully initialized {@link DeactivateAccountCommand} ready for execution
     * @throws NullPointerException if {@code rib} is null
     */
    @Contract("_ -> new")
    public static @NotNull DeactivateAccountCommand buildDeactivate(final @NotNull String rib) {
        return new DeactivateAccountCommand(
                rib,
                LocalDateTime.now(),
                SecurityUtils.getCurrentUsername()
        );
    }
}