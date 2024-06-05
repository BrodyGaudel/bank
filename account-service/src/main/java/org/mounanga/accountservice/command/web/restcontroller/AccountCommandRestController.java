package org.mounanga.accountservice.command.web.restcontroller;

import jakarta.validation.Valid;

import org.mounanga.accountservice.command.dto.*;
import org.mounanga.accountservice.command.service.AccountCommandService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/commands")
public class AccountCommandRestController {

    private final AccountCommandService commandService;

    public AccountCommandRestController(AccountCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody @Valid CreateAccountRequest request){
        return commandService.createAccount(request);
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody @Valid CreditAccountRequest request){
        return commandService.creditAccount(request);
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody @Valid DebitAccountRequest request){
        return commandService.debitAccount(request);
    }

    @PutMapping("/transfer")
    public CompletableFuture<String> transferBetweenAccount(@RequestBody @Valid TransferRequest request){
        return commandService.transferBetweenAccount(request);
    }

    @PutMapping("/update")
    public CompletableFuture<String> updateAccountStatus(@RequestBody @Valid UpdateAccountStatusRequest request){
        return commandService.updateAccountStatus(request);
    }

    @DeleteMapping("/delete/{id}")
    public CompletableFuture<String> deleteAccount(@PathVariable(name = "id") String accountId){
        return commandService.deleteAccount(accountId);
    }

}
