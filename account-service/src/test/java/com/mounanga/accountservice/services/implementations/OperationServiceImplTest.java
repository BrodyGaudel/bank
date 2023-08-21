package com.mounanga.accountservice.services.implementations;

import com.mounanga.accountservice.dtos.CreditDTO;
import com.mounanga.accountservice.dtos.DebitDTO;
import com.mounanga.accountservice.dtos.HistoryDTO;
import com.mounanga.accountservice.dtos.OperationDTO;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.Operation;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.entities.builders.OperationBuilder;
import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.OperationType;
import com.mounanga.accountservice.exceptions.AccountNotActivatedException;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.BalanceNotSufficientException;
import com.mounanga.accountservice.exceptions.OperationNotFoundException;
import com.mounanga.accountservice.mappers.implementations.MappersImpl;
import com.mounanga.accountservice.repositories.AccountRepository;
import com.mounanga.accountservice.repositories.OperationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OperationServiceImplTest {

    private OperationServiceImpl service;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private AccountRepository accountRepository;


    @BeforeEach
    void setUp() {
        service = new OperationServiceImpl(
              operationRepository,
              accountRepository,
              new MappersImpl()
        );
        operationRepository.deleteAll();
        accountRepository.deleteAll();
    }


    @Test
    void testCreditAccount() throws AccountNotFoundException, BalanceNotSufficientException, AccountNotActivatedException {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setId(id)
                .setOperations( new ArrayList<>())
                .setLastUpdate( date)
                .setCreation( date)
                .setStatus(AccountStatus.ACTIVATED)
                .setCurrency(Currency.ZAR)
                .setBalance(BigDecimal.valueOf(5000.00))
                .setCustomerId(id)
                .build();
        Account accountSaved = accountRepository.save(account);

        CreditDTO request = new CreditDTO(accountSaved.getId(), "test", BigDecimal.valueOf(5000.00));
        CreditDTO response = service.creditAccount(request);
        assertNotNull(response);
        Account accountFound = accountRepository.findById(accountSaved.getId()).orElse(null);
        assertNotNull(accountFound);
        double balance = accountFound.getBalance().doubleValue();
        double value = 10000.00;
        assertEquals(value, balance);
    }

    @Test
    void testDebitAccount() throws AccountNotActivatedException, AccountNotFoundException, BalanceNotSufficientException {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setId(id)
                .setOperations( new ArrayList<>())
                .setLastUpdate( date)
                .setCreation( date)
                .setStatus(AccountStatus.ACTIVATED)
                .setCurrency(Currency.ZAR)
                .setBalance(BigDecimal.valueOf(5000))
                .setCustomerId(id)
                .build();
        Account accountSaved = accountRepository.save(account);

        DebitDTO request = new DebitDTO(accountSaved.getId(), "test", BigDecimal.valueOf(2500));
        DebitDTO response = service.debitAccount(request);
        assertNotNull(response);
        Account accountFound = accountRepository.findById(accountSaved.getId()).orElse(null);
        assertNotNull(accountFound);
        double balance = accountFound.getBalance().doubleValue();
        double value = 2500.00;
        assertEquals(value, balance);
    }

    @Test
    void testGetOperationById() throws OperationNotFoundException {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setId(id)
                .setOperations( new ArrayList<>())
                .setLastUpdate( date)
                .setCreation( date)
                .setStatus(AccountStatus.ACTIVATED)
                .setCurrency(Currency.ZAR)
                .setBalance(BigDecimal.valueOf(5000))
                .setCustomerId(id)
                .build();
        Account accountSaved = accountRepository.save(account);

        Operation operation = new OperationBuilder()
                .setId(id)
                .setAccount(accountSaved)
                .setAmount(BigDecimal.valueOf(6000))
                .setDate(date)
                .setType(OperationType.CREDIT)
                .setDescription("TEST")
                .setCurrency(Currency.ZAR)
                .build();

        Operation operationSaved = operationRepository.save(operation);

        OperationDTO response = service.getOperationById(operationSaved.getId());
        assertNotNull(response);
        assertEquals(response.id(), operationSaved.getId());
    }

    @Test
    void testGetAccountHistory() throws AccountNotFoundException {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setId(id)
                .setOperations( new ArrayList<>())
                .setLastUpdate( date)
                .setCreation( date)
                .setStatus(AccountStatus.ACTIVATED)
                .setCurrency(Currency.ZAR)
                .setBalance(BigDecimal.valueOf(5000))
                .setCustomerId(id)
                .build();
        Account accountSaved = accountRepository.save(account);

        Operation operation = new OperationBuilder()
                .setId(id)
                .setAccount(accountSaved)
                .setAmount(BigDecimal.valueOf(6000))
                .setDate(date)
                .setType(OperationType.CREDIT)
                .setDescription("TEST")
                .setCurrency(Currency.ZAR)
                .build();

        operationRepository.save(operation);

        HistoryDTO response = service.getAccountHistory(
                accountSaved.getId(),0,1
        );
        assertNotNull(response);
        assertEquals(response.accountId(), accountSaved.getId());
        assertEquals(1, response.operations().size());
    }
}