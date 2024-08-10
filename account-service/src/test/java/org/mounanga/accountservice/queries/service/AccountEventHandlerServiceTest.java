package org.mounanga.accountservice.queries.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.accountservice.common.enums.AccountStatus;
import org.mounanga.accountservice.common.enums.Currency;
import org.mounanga.accountservice.common.enums.OperationType;
import org.mounanga.accountservice.common.event.*;
import org.mounanga.accountservice.queries.entity.Account;
import org.mounanga.accountservice.queries.entity.Operation;
import org.mounanga.accountservice.queries.reposiory.AccountRepository;
import org.mounanga.accountservice.queries.reposiory.OperationRepository;
import org.mounanga.accountservice.queries.util.notification.NotificationService;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountEventHandlerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AccountEventHandlerService accountEventHandlerService;
    Account account;

    @BeforeEach
    void setUp() {
        this.accountEventHandlerService = new AccountEventHandlerService(accountRepository, operationRepository, notificationService);
        account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(Currency.EUR);
        account.setStatus(AccountStatus.CREATED);
        account.setCustomerId("customerId");
        account.setEmail("customer@bank.com");
        account.setId("accountId");
        account.setCreatedBy("system");
    }

    @Test
    void testHandleAccountCreatedEvent() {
        LocalDateTime now = LocalDateTime.now();
        AccountCreatedEvent event = new AccountCreatedEvent(
                "accountId", now, "system", AccountStatus.CREATED, BigDecimal.ZERO, Currency.EUR, "customerId","customer@bank.com"
        );
        account.setCreatedDate(now);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        doNothing().when(notificationService).sentAccountCreationNotification(anyString(), anyString(), any());

        Account result = accountEventHandlerService.handleAccountCreatedEvent(event);
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getCustomerId(), result.getCustomerId());
        assertEquals(event.getEmail(), result.getEmail());
        assertEquals(event.getStatus(), result.getStatus());
        assertEquals(event.getBalance(), result.getBalance());
        assertEquals(event.getCurrency(), result.getCurrency());
        assertEquals(event.getEventBy(), result.getCreatedBy());
        assertEquals(event.getEventDate(), result.getCreatedDate());
    }

    @Test
    void testHandleAccountActivatedEvent(){
        AccountActivatedEvent event = new AccountActivatedEvent("accountId", LocalDateTime.now(), "system", AccountStatus.ACTIVATED);
        Account activatedAccount = account;
        activatedAccount.setStatus(AccountStatus.ACTIVATED);
        activatedAccount.setLastModifiedBy("system");
        activatedAccount.setLastModifiedDate(event.getEventDate());
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(activatedAccount);
        doNothing().when(notificationService).sendAccountActivationNotification(anyString(), any());

        Account result = accountEventHandlerService.handleAccountActivatedEvent(event);
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getStatus(), result.getStatus());
        assertEquals(event.getEventBy(), result.getLastModifiedBy());
        assertEquals(event.getEventDate(), result.getLastModifiedDate());
    }

    @Test
    void testHandleAccountSuspendedEvent(){
        AccountSuspendedEvent event = new AccountSuspendedEvent("accountId", LocalDateTime.now(), "system", AccountStatus.SUSPENDED);
        Account activatedAccount = account;
        activatedAccount.setStatus(AccountStatus.SUSPENDED);
        activatedAccount.setLastModifiedBy("system");
        activatedAccount.setLastModifiedDate(event.getEventDate());
        when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(activatedAccount);
        doNothing().when(notificationService).sendAccountSuspensionNotification(anyString(), any());

        Account result = accountEventHandlerService.handleAccountSuspendedEvent(event);
        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getStatus(), result.getStatus());
        assertEquals(event.getEventBy(), result.getLastModifiedBy());
        assertEquals(event.getEventDate(), result.getLastModifiedDate());
    }

    @Test
    void testHandleAccountDeletedEvents() {
        AccountDeletedEvent event = new AccountDeletedEvent("accountId", LocalDateTime.now(), "system" );
        account.setLastModifiedBy(event.getEventBy());
        when(accountRepository.findById(event.getId())).thenReturn(Optional.of(account));

        accountEventHandlerService.handleAccountDeletedEvent(event);

        verify(operationRepository).deleteByAccountId(account.getId());
        verify(accountRepository).deleteById(account.getId());
        verify(notificationService).sendAccountDeletedNotification(account.getId(), account.getEmail(), event.getEventDate());
    }

    @Test
    void testHandleAccountCreditedEvent() {
        // Given
        String accountId = "account";
        LocalDateTime eventDate = LocalDateTime.now();
        AccountCreditedEvent event = new AccountCreditedEvent(
                accountId,
                eventDate,
                "system",
                new BigDecimal("100.00"),
                OperationType.CREDIT,
                "Deposit"
        );
        account.setBalance(new BigDecimal("500.00"));

        // Mocking the findAccountById method
        when(accountRepository.findById(event.getId())).thenReturn(Optional.of(account));
        // Mocking the accountRepository.save method
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        // Mocking the operationRepository.save method
        Operation operation = new Operation();
        operation.setId("operationId");
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        // When
        Operation result = accountEventHandlerService.handleAccountCreditedEvent(event);

        // Then
        verify(accountRepository).findById(event.getId());
        verify(accountRepository).save(account);
        verify(operationRepository).save(any(Operation.class));
        verify(notificationService).sendAccountCreditedNotification(
                account.getEmail(),
                event.getAmount(),
                account.getBalance(),
                eventDate
        );

        // Assert the returned Operation
        assertEquals("operationId", result.getId());
    }

    @Test
    void testHandleAccountDebitedEvent() {
        // Given
        String accountId = "account";
        LocalDateTime eventDate = LocalDateTime.now();
        AccountDebitedEvent event = new AccountDebitedEvent(
                accountId,
                eventDate,
                "system",
                new BigDecimal("100.00"),
                OperationType.DEBIT,
                "Withdrawal"
        );
        account.setBalance(new BigDecimal("500.00"));

        // Mocking the findAccountById method
        when(accountRepository.findById(event.getId())).thenReturn(Optional.of(account));
        // Mocking the accountRepository.save method
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        // Mocking the operationRepository.save method
        Operation operation = new Operation();
        operation.setId("operationId");
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        // When
        Operation result = accountEventHandlerService.handleAccountDebitedEvent(event);

        // Then
        verify(accountRepository).findById(event.getId());
        verify(accountRepository).save(account);
        verify(operationRepository).save(any(Operation.class));
        verify(notificationService).sendAccountDebitedNotification(
                account.getEmail(),
                event.getAmount(),
                account.getBalance(),
                eventDate
        );

        // Assert the returned Operation
        assertEquals("operationId", result.getId());
    }

}