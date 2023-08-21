package com.mounanga.accountservice.repositories;

import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.Operation;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.entities.builders.OperationBuilder;
import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.enums.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OperationRepositoryTest {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void testFindByAccountIdOrderByDateDesc() {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setId(id)
                .setOperations( new ArrayList<>())
                .setLastUpdate( date)
                .setCreation( date)
                .setStatus(AccountStatus.ACTIVATED)
                .setCurrency(Currency.TZS)
                .setBalance(BigDecimal.valueOf(5000))
                .setCustomerId(id)
                .build();
        Account accountSaved = accountRepository.save(account);

        Operation operation1 = new OperationBuilder()
                .setId("operation1")
                .setAccount(accountSaved)
                .setDate(date)
                .setAmount(BigDecimal.valueOf(5000))
                .setCurrency(Currency.TZS)
                .setDescription("CREDIT")
                .setType(OperationType.CREDIT)
                .build();

        Operation operation2 = new OperationBuilder()
                .setId("operation2")
                .setAccount(accountSaved)
                .setDate(date)
                .setAmount(BigDecimal.valueOf(1000))
                .setCurrency(Currency.TZS)
                .setDescription("DEBIT")
                .setType(OperationType.DEBIT)
                .build();

        operationRepository.saveAll(List.of(operation1, operation2));

        Pageable pageable = PageRequest.of(0, 2);
        Page<Operation> operationsPage = operationRepository
                .findByAccountIdOrderByDateDesc(accountSaved.getId(), pageable);
        assertNotNull(operationsPage);
        assertEquals(2, operationsPage.getSize());
    }
}