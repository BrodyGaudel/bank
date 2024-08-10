package org.mounanga.accountservice.commands.util.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.commands.command.*;
import org.mounanga.accountservice.commands.dto.OperationRequestDTO;
import org.mounanga.accountservice.commands.dto.UpdateStatusRequestDTO;
import org.mounanga.accountservice.common.enums.AccountStatus;
import org.mounanga.accountservice.common.enums.OperationType;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommandFactory {

    private CommandFactory() {
        super();
    }

    @NotNull
    @Contract("_, _ -> new")
    public static CreditAccountCommand createCreditAccountCommand(@NotNull final OperationRequestDTO dto, final String username){
        return new CreditAccountCommand(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                username,
                dto.amount(),
                OperationType.CREDIT,
                dto.description()
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static DebitAccountCommand createDebitAccountCommand(@NotNull final OperationRequestDTO dto, final String username){
        return new DebitAccountCommand(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                username,
                dto.amount(),
                OperationType.DEBIT,
                dto.description()
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static ActivateAccountCommand createActivateAccountCommand(@NotNull final UpdateStatusRequestDTO dto, final String username){
        return new ActivateAccountCommand(
                dto.accountId(),
                LocalDateTime.now(),
                username,
                AccountStatus.ACTIVATED
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static SuspendAccountCommand createSuspendAccountCommand(@NotNull final UpdateStatusRequestDTO dto, final String username){
        return new SuspendAccountCommand(
                dto.accountId(),
                LocalDateTime.now(),
                username,
                AccountStatus.SUSPENDED
        );
    }

    @NotNull
    @Contract("_, _ -> new")
    public static DeleteAccountCommand createDeleteAccountCommand(@NotNull final String accountId, final String username){
        return new DeleteAccountCommand(accountId,LocalDateTime.now(), username);
    }

}
