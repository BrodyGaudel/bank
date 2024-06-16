package org.mounanga.accountservice.command.web.restcontroller;

import jakarta.validation.Valid;

import org.mounanga.accountservice.command.dto.*;
import org.mounanga.accountservice.command.service.AccountCommandService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/commands")
public class AccountCommandRestController {

    private final AccountCommandService commandService;

    public AccountCommandRestController(AccountCommandService commandService) {
        this.commandService = commandService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody @Valid CreateAccountRequest request){
        return commandService.createAccount(request);
    }

    @PreAuthorize("hasAnyAuthority('USER','MODERATOR','ADMIN','SUPER_ADMIN')")
    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody @Valid CreditAccountRequest request){
        return commandService.creditAccount(request);
    }

    @PreAuthorize("hasAnyAuthority('USER','MODERATOR','ADMIN','SUPER_ADMIN')")
    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody @Valid DebitAccountRequest request){
        return commandService.debitAccount(request);
    }

    @PreAuthorize("hasAnyAuthority('USER','MODERATOR','ADMIN','SUPER_ADMIN')")
    @PutMapping("/transfer")
    public CompletableFuture<String> transferBetweenAccount(@RequestBody @Valid TransferRequest request){
        return commandService.transferBetweenAccount(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @PutMapping("/update")
    public CompletableFuture<String> updateAccountStatus(@RequestBody @Valid UpdateAccountStatusRequest request){
        return commandService.updateAccountStatus(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','SUPER_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<String> deleteAccount(@PathVariable(name = "id") String accountId){
        return commandService.deleteAccount(accountId);
    }

}
