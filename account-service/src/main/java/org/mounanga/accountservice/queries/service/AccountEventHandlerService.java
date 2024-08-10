package org.mounanga.accountservice.queries.service;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.mounanga.accountservice.common.enums.AccountStatus;
import org.mounanga.accountservice.common.enums.OperationType;
import org.mounanga.accountservice.common.event.*;
import org.mounanga.accountservice.queries.entity.Account;
import org.mounanga.accountservice.queries.entity.Operation;
import org.mounanga.accountservice.queries.exception.AccountNotFoundException;
import org.mounanga.accountservice.queries.reposiory.AccountRepository;
import org.mounanga.accountservice.queries.reposiory.OperationRepository;
import org.mounanga.accountservice.queries.util.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class AccountEventHandlerService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final NotificationService notificationService;

    public AccountEventHandlerService(AccountRepository accountRepository, OperationRepository operationRepository, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.notificationService = notificationService;
    }

    @EventHandler
    public Account handleAccountCreatedEvent(@NotNull AccountCreatedEvent event) {
        log.info("AccountCreatedEvent received");
        Account account = buildNewAccount(event);
        Account savedAccount = accountRepository.save(account);
        log.info("Account saved with id {} by {} at {}", savedAccount.getId(), savedAccount.getCreatedBy(), savedAccount.getCreatedDate());
        notificationService.sentAccountCreationNotification(event.getId(), event.getEmail(), event.getEventDate());
        return savedAccount;
    }

    @EventHandler
    public Account handleAccountActivatedEvent(@NotNull AccountActivatedEvent event) {
        log.info("AccountActivatedEvent received");
        Account account = findAccountById(event.getId());
        updateAccountStatus(account, event.getStatus(), event.getEventBy(), event.getEventDate());
        Account activatedAccount = accountRepository.save(account);
        log.info("Account with id {} activated by {} at {}", activatedAccount.getId(), activatedAccount.getLastModifiedBy(), activatedAccount.getLastModifiedDate());
        notificationService.sendAccountActivationNotification(account.getEmail(), event.getEventDate());
        return activatedAccount;
    }

    @EventHandler
    public Account handleAccountSuspendedEvent(@NotNull AccountSuspendedEvent event) {
        log.info("AccountSuspendedEvent received");
        Account account = findAccountById(event.getId());
        updateAccountStatus(account, event.getStatus(), event.getEventBy(), event.getEventDate());
        Account suspendedAccount = accountRepository.save(account);
        log.info("Account with id {} suspended by {} at {}", suspendedAccount.getId(), suspendedAccount.getLastModifiedBy(), suspendedAccount.getLastModifiedDate());
        notificationService.sendAccountSuspensionNotification(account.getEmail(), event.getEventDate());
        return suspendedAccount;
    }

    @EventHandler
    public void handleAccountDeletedEvent(@NotNull AccountDeletedEvent event) {
        log.info("AccountDeletedEvent received");
        Account account = findAccountById(event.getId());
        operationRepository.deleteByAccountId(account.getId());
        log.info("All operations linked to the account with id {} have been deleted.", event.getId());
        accountRepository.deleteById(account.getId());
        log.info("Account with id {} deleted by {} at {}", event.getId(), event.getEventBy(), event.getEventDate());
        notificationService.sendAccountDeletedNotification(event.getId(), account.getEmail(), event.getEventDate());
    }

    @EventHandler
    public Operation handleAccountCreditedEvent(@NotNull AccountCreditedEvent event){
        log.info("AccountCreditedEvent received");
        Account account = findAccountById(event.getId());
        updateAccountBalance(account, event.getAmount(), event.getEventBy(), event.getEventDate(), true);
        Account creditedAccount = accountRepository.save(account);
        log.info("Account with id '{}' credited with amount '{}' at '{}' by {}", creditedAccount.getId(), event.getAmount(), creditedAccount.getLastModifiedDate(), creditedAccount.getLastModifiedBy());
        Operation operation = createOperation(creditedAccount, event.getAmount(), event.getType(), event.getDescription(), event.getEventBy(), event.getEventDate());
        Operation creditOperation = operationRepository.save(operation);
        log.info("Credit Operation saved with id '{}'", creditOperation.getId());
        notificationService.sendAccountCreditedNotification(creditedAccount.getEmail(), event.getAmount(), creditedAccount.getBalance(), event.getEventDate());
        return creditOperation;
    }

    @EventHandler
    public Operation handleAccountDebitedEvent(@NotNull AccountDebitedEvent event){
        log.info("AccountDebitedEvent received");
        Account account = findAccountById(event.getId());
        updateAccountBalance(account, event.getAmount(), event.getEventBy(), event.getEventDate(), false);
        Account debitedAccount = accountRepository.save(account);
        log.info("Account with id '{}' debited with amount '{}' at '{}' by {}", debitedAccount.getId(), event.getAmount(), debitedAccount.getLastModifiedDate(), debitedAccount.getLastModifiedBy());
        Operation operation = createOperation(debitedAccount, event.getAmount(), event.getType(), event.getDescription(), event.getEventBy(), event.getEventDate());
        Operation debitOperation = operationRepository.save(operation);
        log.info("Debit Operation saved with id '{}'", debitOperation.getId());
        notificationService.sendAccountDebitedNotification(debitedAccount.getEmail(), event.getAmount(), debitedAccount.getBalance(), event.getEventDate());
        return debitOperation;
    }

    private Account buildNewAccount(@NotNull AccountCreatedEvent event) {
        return Account.builder()
                .id(event.getId())
                .email(event.getEmail())
                .customerId(event.getCustomerId())
                .currency(event.getCurrency())
                .status(event.getStatus())
                .balance(event.getBalance())
                .createdBy(event.getEventBy())
                .createdDate(event.getEventDate())
                .build();
    }

    private void updateAccountStatus(@NotNull Account account, AccountStatus status, String modifiedBy, LocalDateTime modifiedDate) {
        account.setStatus(status);
        account.setLastModifiedBy(modifiedBy);
        account.setLastModifiedDate(modifiedDate);
    }

    private void updateAccountBalance(@NotNull Account account, BigDecimal amount, String modifiedBy, LocalDateTime modifiedDate, boolean isCredit) {
        account.setLastModifiedBy(modifiedBy);
        account.setLastModifiedDate(modifiedDate);
        account.setBalance(isCredit ? account.getBalance().add(amount) : account.getBalance().subtract(amount));
    }

    private Operation createOperation(Account account, BigDecimal amount, OperationType type, String description, String createdBy, LocalDateTime dateTime) {
        return Operation.builder()
                .account(account)
                .amount(amount)
                .createdBy(createdBy)
                .dateTime(dateTime)
                .type(type)
                .description(description)
                .build();
    }

    private Account findAccountById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", id)));
    }
}

