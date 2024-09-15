package org.mounanga.accountservice.commands.util.proxy;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.commands.command.CreditAccountCommand;
import org.mounanga.accountservice.commands.command.DebitAccountCommand;
import org.mounanga.accountservice.commands.dto.TransferRequestDTO;
import org.mounanga.accountservice.commands.util.factory.CommandFactory;
import org.mounanga.accountservice.common.security.SecurityInformation;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
public class TransferProxy {

    public TransferProxy(){
        super();
    }

    public List<CompletableFuture<String>> transfer(final TransferRequestDTO dto, @NotNull CommandGateway commandGateway, @NotNull SecurityInformation securityInformation){
        log.info("Transfer request: {}", dto);

        DebitAccountCommand debitCommand = CommandFactory.createDebitAccountCommand(dto, securityInformation.getUsername());
        CompletableFuture<String> debited = commandGateway.send(debitCommand);
        debited.join();
        log.info("Debited success: {}", debited);

        try{
            CreditAccountCommand creditCommand = CommandFactory.createCreditAccountCommand(dto, securityInformation.getUsername());
            CompletableFuture<String> credited = commandGateway.send(creditCommand);
            credited.join();
            log.info("Credited success: {}", credited);
            return List.of(debited, credited);
        }catch (Exception e){
            log.warn(e.getMessage());
            CreditAccountCommand creditCommand = CommandFactory.createCreditAccountCommandReverse(dto, securityInformation.getUsername());
            CompletableFuture<String> credited = commandGateway.send(creditCommand);
            credited.join();
            log.info("System compensation: {}", credited);
            return List.of(debited, credited);
        }
    }
}
