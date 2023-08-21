package com.mounanga.accountservice.services.implementations;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.enums.Currency;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.mappers.implementations.MappersImpl;
import com.mounanga.accountservice.repositories.AccountRepository;
import com.mounanga.accountservice.repositories.CompterRepository;
import com.mounanga.accountservice.repositories.OperationRepository;
import com.mounanga.accountservice.restclients.CustomerRestClient;
import com.mounanga.accountservice.utils.implementations.IdGeneratorImpl;
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
class AccountServiceImplTest {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private CustomerRestClient customerRestClient;

    @Autowired
    private CompterRepository compterRepository;

    @Autowired
    private OperationRepository operationRepository;

    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        IdGeneratorImpl idGenerator = new IdGeneratorImpl(compterRepository);
        service = new AccountServiceImpl(repository, new MappersImpl(), customerRestClient, idGenerator);
        operationRepository.deleteAll();
        repository.deleteAll();
    }


    @Test
    void testGetAccountById() throws AccountNotFoundException {
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
        Account accountSaved = repository.save(account);
        AccountDTO response = service.getAccountById(accountSaved.getId());
        System.out.println(response.id());
        System.out.println(accountSaved.getId());
        assertNotNull(response);
        assertEquals(response.id(), accountSaved.getId());
    }

    @Test
    void testGetAccountByCustomerId() throws AccountNotFoundException {
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
        Account accountSaved = repository.save(account);
        AccountDTO response = service.getAccountByCustomerId(accountSaved.getCustomerId());
        assertNotNull(response);
        assertEquals(response.customerId(), accountSaved.getCustomerId());
        assertEquals(response.id(), accountSaved.getId());
    }

    @Test
    void testDeleteAccountById() {
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
        Account accountSaved = repository.save(account);
        service.deleteAccountById(accountSaved.getId());
        Account response = repository.findById(accountSaved.getId()).orElse(null);
        assertNull(response);
    }
}