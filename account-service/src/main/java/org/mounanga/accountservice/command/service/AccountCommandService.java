package org.mounanga.accountservice.command.service;

import org.mounanga.accountservice.command.dto.*;

import java.util.concurrent.CompletableFuture;

public interface AccountCommandService {

    CompletableFuture<String> createAccount(CreateAccountRequest request);
    CompletableFuture<String> creditAccount(CreditAccountRequest request);
    CompletableFuture<String> debitAccount(DebitAccountRequest request);
    CompletableFuture<String> transferBetweenAccount(TransferRequest request);
    CompletableFuture<String> updateAccountStatus(UpdateAccountStatusRequest request);
    CompletableFuture<String> deleteAccount(String accountId);
}
