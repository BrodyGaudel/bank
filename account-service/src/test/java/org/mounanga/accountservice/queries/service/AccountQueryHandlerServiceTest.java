package org.mounanga.accountservice.queries.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.accountservice.queries.dto.AccountResponseDTO;
import org.mounanga.accountservice.queries.dto.OperationResponseDTO;
import org.mounanga.accountservice.queries.entity.Account;
import org.mounanga.accountservice.queries.entity.Operation;
import org.mounanga.accountservice.queries.query.GetAccountByCustomerIdQuery;
import org.mounanga.accountservice.queries.query.GetAccountByIdQuery;
import org.mounanga.accountservice.queries.query.GetOperationByAccountId;
import org.mounanga.accountservice.queries.query.GetOperationByIdQuery;
import org.mounanga.accountservice.queries.reposiory.AccountRepository;
import org.mounanga.accountservice.queries.reposiory.OperationRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountQueryHandlerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private AccountQueryHandlerService accountQueryHandlerService;

    private Account account;
    private Operation operation;

    @BeforeEach
    void setUp() {
        accountQueryHandlerService = new AccountQueryHandlerService(accountRepository, operationRepository);
        account = new Account();
        account.setId("accountId");
        account.setCustomerId("customerId");

        operation = new Operation();
        operation.setId("operationId");
        operation.setAccount(account);
    }

    @Test
    void testHandleGetAccountByIdQueryFound() {
        // Given
        GetAccountByIdQuery query = new GetAccountByIdQuery("accountId");

        // Mocking the repository call
        when(accountRepository.findById(query.getAccountId())).thenReturn(Optional.of(account));

        // When
        AccountResponseDTO result = accountQueryHandlerService.handle(query);

        // Then
        assertNotNull(result);
        assertEquals("accountId", result.getId());
        verify(accountRepository).findById(query.getAccountId());
    }

    @Test
    void testHandleGetAccountByIdQueryNotFound() {
        // Given
        GetAccountByIdQuery query = new GetAccountByIdQuery("accountId");

        // Mocking the repository call
        when(accountRepository.findById(query.getAccountId())).thenReturn(Optional.empty());

        // When
        AccountResponseDTO result = accountQueryHandlerService.handle(query);

        // Then
        assertNull(result);
        verify(accountRepository).findById(query.getAccountId());
    }

    @Test
    void testHandleGetAccountByCustomerIdQueryFound() {
        // Given
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery("customerId");

        // Mocking the repository call
        when(accountRepository.findByCustomerId(query.getCustomerId())).thenReturn(Optional.of(account));

        // When
        AccountResponseDTO result = accountQueryHandlerService.handle(query);

        // Then
        assertNotNull(result);
        assertEquals("customerId", result.getCustomerId());
        verify(accountRepository).findByCustomerId(query.getCustomerId());
    }

    @Test
    void testHandleGetAccountByCustomerIdQueryNotFound() {
        // Given
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery("customerId");

        // Mocking the repository call
        when(accountRepository.findByCustomerId(query.getCustomerId())).thenReturn(Optional.empty());

        // When
        AccountResponseDTO result = accountQueryHandlerService.handle(query);

        // Then
        assertNull(result);
        verify(accountRepository).findByCustomerId(query.getCustomerId());
    }

    @Test
    void testHandleGetOperationByIdQueryFound() {
        // Given
        GetOperationByIdQuery query = new GetOperationByIdQuery("operationId");

        // Mocking the repository call
        when(operationRepository.findById(query.getOperationId())).thenReturn(Optional.of(operation));

        // When
        OperationResponseDTO result = accountQueryHandlerService.handle(query);

        // Then
        assertNotNull(result);
        assertEquals("operationId", result.getId());
        verify(operationRepository).findById(query.getOperationId());
    }

    @Test
    void testHandleGetOperationByIdQueryNotFound() {
        // Given
        GetOperationByIdQuery query = new GetOperationByIdQuery("operationId");

        // Mocking the repository call
        when(operationRepository.findById(query.getOperationId())).thenReturn(Optional.empty());

        // When
        OperationResponseDTO result = accountQueryHandlerService.handle(query);

        // Then
        assertNull(result);
        verify(operationRepository).findById(query.getOperationId());
    }

    @Test
    void testHandleGetOperationByAccountIdQueryFound() {
        // Given
        GetOperationByAccountId query = new GetOperationByAccountId("accountId", 0, 10);
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        Page<Operation> operationPage = new PageImpl<>(Collections.singletonList(operation));

        // Mocking the repository call
        when(operationRepository.findByAccountId(query.getAccountId(), pageable)).thenReturn(operationPage);

        // When
        List<OperationResponseDTO> result = accountQueryHandlerService.handle(query);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(operationRepository).findByAccountId(query.getAccountId(), pageable);
    }

    @Test
    void testHandleGetOperationByAccountIdQueryEmpty() {
        // Given
        GetOperationByAccountId query = new GetOperationByAccountId("accountId", 0, 10);
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        Page<Operation> operationPage = new PageImpl<>(Collections.emptyList());

        // Mocking the repository call
        when(operationRepository.findByAccountId(query.getAccountId(), pageable)).thenReturn(operationPage);

        // When
        List<OperationResponseDTO> result = accountQueryHandlerService.handle(query);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(operationRepository).findByAccountId(query.getAccountId(), pageable);
    }

}