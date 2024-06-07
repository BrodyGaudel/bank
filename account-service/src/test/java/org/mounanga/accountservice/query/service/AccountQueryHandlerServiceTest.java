package org.mounanga.accountservice.query.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.accountservice.query.dto.AccountResponse;
import org.mounanga.accountservice.query.dto.OperationResponse;
import org.mounanga.accountservice.query.entity.Account;
import org.mounanga.accountservice.query.entity.Operation;
import org.mounanga.accountservice.query.queries.GetAccountByCustomerIdQuery;
import org.mounanga.accountservice.query.queries.GetAccountByIdQuery;
import org.mounanga.accountservice.query.queries.GetAllOperationByAccountIdQuery;
import org.mounanga.accountservice.query.queries.GetOperationByIdQuery;
import org.mounanga.accountservice.query.repository.AccountRepository;
import org.mounanga.accountservice.query.repository.OperationRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class AccountQueryHandlerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private AccountQueryHandlerService accountQueryHandlerService;

    @BeforeEach
    void setUp() {
        this.accountQueryHandlerService = new AccountQueryHandlerService(
                accountRepository,
                operationRepository
        );
    }

    @Test
    void handleGetAccountByIdQuery() {
        GetAccountByIdQuery query = new GetAccountByIdQuery("1");
        Account account = new Account();
        account.setId("1");

        when(accountRepository.findById(query.getAccountId())).thenReturn(Optional.of(account));

        AccountResponse response = accountQueryHandlerService.handle(query);

        verify(accountRepository, times(1)).findById(query.getAccountId());
        assertNotNull(response);
        assertEquals("1", response.getId());
    }

    @Test
    void handleGetAccountByIdQuery_NotFound() {
        GetAccountByIdQuery query = new GetAccountByIdQuery("1");

        when(accountRepository.findById(query.getAccountId())).thenReturn(Optional.empty());

        AccountResponse response = accountQueryHandlerService.handle(query);

        verify(accountRepository, times(1)).findById(query.getAccountId());
        assertNull(response);
    }

    @Test
    void handleGetAccountByCustomerIdQuery() {
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery("customer1");
        Account account = new Account();
        account.setCustomerId("customer1");

        when(accountRepository.findByCustomerId(query.getCustomerId())).thenReturn(Optional.of(account));

        AccountResponse response = accountQueryHandlerService.handle(query);

        verify(accountRepository, times(1)).findByCustomerId(query.getCustomerId());
        assertNotNull(response);
        assertEquals("customer1", response.getCustomerId());
    }

    @Test
    void handleGetAccountByCustomerIdQuery_NotFound() {
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery("customer1");

        when(accountRepository.findByCustomerId(query.getCustomerId())).thenReturn(Optional.empty());

        AccountResponse response = accountQueryHandlerService.handle(query);

        verify(accountRepository, times(1)).findByCustomerId(query.getCustomerId());
        assertNull(response);
    }

    @Test
    void handleGetOperationByIdQuery() {
        GetOperationByIdQuery query = new GetOperationByIdQuery("1");
        Operation operation = new Operation();
        operation.setId("1");

        when(operationRepository.findById(query.getOperationId())).thenReturn(Optional.of(operation));

        OperationResponse response = accountQueryHandlerService.handle(query);

        verify(operationRepository, times(1)).findById(query.getOperationId());
        assertNotNull(response);
        assertEquals("1", response.getId());
    }

    @Test
    void handleGetOperationByIdQuery_NotFound() {
        GetOperationByIdQuery query = new GetOperationByIdQuery("1");

        when(operationRepository.findById(query.getOperationId())).thenReturn(Optional.empty());

        OperationResponse response = accountQueryHandlerService.handle(query);

        verify(operationRepository, times(1)).findById(query.getOperationId());
        assertNull(response);
    }

    @Test
    void handleGetAllOperationByAccountIdQuery() {
        GetAllOperationByAccountIdQuery query = new GetAllOperationByAccountIdQuery("1", 0, 10);
        Operation operation1 = new Operation();
        operation1.setId("1");
        Operation operation2 = new Operation();
        operation2.setId("2");
        List<Operation> operations = List.of(operation1, operation2);
        Page<Operation> operationPage = new PageImpl<>(operations);

        when(operationRepository.findByAccountId(eq(query.getAccountId()), any(PageRequest.class)))
                .thenReturn(operationPage);

        List<OperationResponse> responses = accountQueryHandlerService.handle(query);

        verify(operationRepository, times(1)).findByAccountId(eq(query.getAccountId()), any(PageRequest.class));
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("1", responses.get(0).getId());
        assertEquals("2", responses.get(1).getId());
    }

}