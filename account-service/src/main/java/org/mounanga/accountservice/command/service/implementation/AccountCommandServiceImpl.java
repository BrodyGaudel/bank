package org.mounanga.accountservice.command.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.command.commands.*;
import org.mounanga.accountservice.command.dto.*;
import org.mounanga.accountservice.common.enums.Status;
import org.mounanga.accountservice.command.exception.CustomerNotFoundException;
import org.mounanga.accountservice.command.service.AccountCommandService;
import org.mounanga.accountservice.command.util.AccountIdGenerator;
import org.mounanga.accountservice.command.web.restclient.CustomerRestClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AccountCommandServiceImpl implements AccountCommandService {

    private static final String ROLL_BACK_MESSAGE = "The transfer failed: the destination account is not ready to receive money. If the problem persists, please contact the administrator.";

    private final CommandGateway commandGateway;
    private final AccountIdGenerator generator;
    private final CustomerRestClient restClient;

    public AccountCommandServiceImpl(CommandGateway commandGateway, AccountIdGenerator generator, CustomerRestClient restClient) {
        this.commandGateway = commandGateway;
        this.generator = generator;
        this.restClient = restClient;
    }

    @Override
    public CompletableFuture<String> createAccount(@NotNull CreateAccountRequest request) {
        log.info("In createAccount");
        if(!customerExists(request.customerId())){
            throw new CustomerNotFoundException("customer with id '"+request.customerId()+"' not found");
        }
        return commandGateway.send(new CreateAccountCommand(
                generator.generateAccountId(),
                Status.CREATED,
                BigDecimal.ZERO,
                request.currency(),
                request.customerId(),
                LocalDateTime.now(),
                getCommander()
        ));
    }

    @Override
    public CompletableFuture<String> creditAccount(@NotNull CreditAccountRequest request) {
        log.info("In creditAccount");
        return commandGateway.send(new CreditAccountCommand(
                request.accountId(),
                request.description(),
                request.amount(),
                LocalDateTime.now(),
                getCommander()
        ));
    }

    @Override
    public CompletableFuture<String> debitAccount(@NotNull DebitAccountRequest request) {
        log.info("In debitAccount");
        return commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.description(),
                request.amount(),
                LocalDateTime.now(),
                getCommander()
        ));
    }

    @Override
    public CompletableFuture<String> transferBetweenAccount(@NotNull TransferRequest request) {
        log.info("In transferBetweenAccount");
        String debitMessage = "Transfer '"+request.amount()+"' To '"+request.accountIdTo()+"' | "+request.description() ;

        CompletableFuture<String> debitFuture = debitAccount(new DebitAccountRequest(
                request.accountIdFrom(),
                debitMessage,
                request.amount()
        ));
        debitFuture.join();
        try{
            String creditMessage = "Transfer '"+request.amount()+"' From '"+request.accountIdFrom()+"' | "+request.description() ;
            CompletableFuture<String> creditFuture = creditAccount(new CreditAccountRequest(
                    request.accountIdTo(),
                    creditMessage,
                    request.amount()
            ));
            creditFuture.join();
            return creditFuture;
        }catch (Exception e){
            log.warn(e.getMessage());
            log.error(e.getLocalizedMessage());
            return creditAccount(new CreditAccountRequest(
                    request.accountIdFrom(),
                    ROLL_BACK_MESSAGE,
                    request.amount()
            ));
        }

    }

    @Override
    public CompletableFuture<String> updateAccountStatus(@NotNull UpdateAccountStatusRequest request) {
        log.info("In updateAccountStatus");
        if(request.status().equals(Status.ACTIVATED)){
            return commandGateway.send(new ActivateAccountCommand(
                    request.accountId(),
                    Status.ACTIVATED,
                    LocalDateTime.now(),
                    getCommander()
            ));
        } else {
            return commandGateway.send(new SuspendAccountCommand(
                    request.accountId(),
                    Status.SUSPENDED,
                    LocalDateTime.now(),
                    getCommander()
            ));
        }
    }

    @Override
    public CompletableFuture<String> deleteAccount(String accountId) {
        log.info("In deleteAccount");
        return commandGateway.send(new DeleteAccountCommand(accountId));
    }

    @NotNull
    @Contract(pure = true)
    private String getCommander(){
        return "username";
    }

    private boolean customerExists(String customerId){
        try {
            return restClient.customerExist(customerId).exists();
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
