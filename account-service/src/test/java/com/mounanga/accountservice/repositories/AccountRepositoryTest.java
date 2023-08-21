package com.mounanga.accountservice.repositories;

import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.enums.Currency;
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
class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private OperationRepository operationRepository;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    void testFindByCustomerId() {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setId(id)
                .setOperations( new ArrayList<>())
                .setLastUpdate( date)
                .setCreation( date)
                .setStatus(AccountStatus.ACTIVATED)
                .setCurrency(Currency.TND)
                .setBalance(BigDecimal.valueOf(5000))
                .setCustomerId(id)
                .build();
        repository.save(account);
        Account response = repository.findByCustomerId(id);
        assertNotNull(response);
        assertEquals(account.getId(), response.getId());
        assertEquals(account.getCustomerId(), response.getCustomerId());
    }

    @Test
    void testCheckIfCustomerIdExists() {
        String id = UUID.randomUUID().toString();
        Date date = new Date();
        Account account = new AccountBuilder()
                .setId(id)
                .setOperations( new ArrayList<>())
                .setLastUpdate( date)
                .setCreation( date)
                .setStatus(AccountStatus.ACTIVATED)
                .setCurrency(Currency.MAD)
                .setBalance(BigDecimal.valueOf(5000))
                .setCustomerId(id)
                .build();
        repository.save(account);
        Boolean response1 = repository.checkIfCustomerIdExists(account.getCustomerId());
        Boolean response2 = repository.checkIfCustomerIdExists(account.getCustomerId()+"1010");
        assertEquals(Boolean.TRUE, response1);
        assertNotEquals(Boolean.TRUE, response2);
    }
}