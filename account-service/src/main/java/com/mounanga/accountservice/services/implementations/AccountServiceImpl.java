package com.mounanga.accountservice.services.implementations;

import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.dtos.CustomerDTO;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.builders.AccountBuilder;
import com.mounanga.accountservice.enums.AccountStatus;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.CustomerNotFoundException;
import com.mounanga.accountservice.mappers.Mappers;
import com.mounanga.accountservice.repositories.AccountRepository;
import com.mounanga.accountservice.restclients.CustomerRestClient;
import com.mounanga.accountservice.services.AccountService;
import com.mounanga.accountservice.utils.IdGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String NOT_FOUND = "' not found.";

    private final AccountRepository repository;
    private final Mappers mappers;
    private final CustomerRestClient customerRestClient;
    private final IdGenerator idGenerator;

    public AccountServiceImpl(AccountRepository repository, Mappers mappers, CustomerRestClient customerRestClient, IdGenerator idGenerator) {
        this.repository = repository;
        this.mappers = mappers;
        this.customerRestClient = customerRestClient;
        this.idGenerator = idGenerator;
    }

    /**
     * Retrieve an account by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account with the given ID is not found.
     */
    @Override
    public AccountDTO getAccountById(String id) throws AccountNotFoundException {
        log.info("In getAccountById()");
        Account account = repository.findById(id)
                .orElseThrow( () -> new AccountNotFoundException("Account with id '"+id+NOT_FOUND));
        log.info("account found");
        return mappers.fromAccount(account);
    }

    /**
     * Retrieve an account by its customer ID.
     *
     * @param customerId The customer ID associated with the account.
     * @return The account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If the account for the given customer ID is not found.
     */
    @Override
    public AccountDTO getAccountByCustomerId(String customerId) throws AccountNotFoundException {
        log.info("In getAccountByCustomerId()");
        Account account = repository.findByCustomerId(customerId);
        if(account == null){
            throw new AccountNotFoundException("Account with customerId '"+customerId+NOT_FOUND);
        }
        log.info("account found");
        return mappers.fromAccount(account);
    }

    /**
     * Create a new account.
     *
     * @param accountDTO The account details to create.
     * @return The created account details as a Data Transfer Object (DTO).
     * @throws CustomerNotFoundException If the customer associated with the account is not found.
     */
    @Override
    public AccountDTO createAccount(@NotNull AccountDTO accountDTO) throws CustomerNotFoundException {
        log.info("In createAccount()");
        CustomerDTO customer = getCustomerById(accountDTO.customerId());
        if(customer == null){
            throw new CustomerNotFoundException("Customer with id '"+accountDTO.customerId()+NOT_FOUND);
        }

        Account account = new AccountBuilder()
                .setCustomerId(customer.id())
                .setBalance(BigDecimal.valueOf(0))
                .setCurrency(accountDTO.currency())
                .setStatus(AccountStatus.CREATED)
                .setOperations( new ArrayList<>())
                .setId(idGenerator.autoGenerate())
                .setLastUpdate(null)
                .setCreation( new Date())
                .build();

        Account accountSaved = repository.save(account);
        log.info("account created successfully");
        return mappers.fromAccount(accountSaved);
    }

    /**
     * Delete an account by its ID.
     *
     * @param id The ID of the account to delete.
     */
    @Override
    public void deleteAccountById(String id) {
        log.info("In deleteAccountById()");
        repository.deleteById(id);
        log.info("Account deleted");
    }

    /**
     * Update Account Status
     *
     * @param accountDTO The account details to update status
     * @return The Updated Account details as a Data Transfer Object (DTO).
     * @throws AccountNotFoundException If account not found
     */
    @Override
    public AccountDTO updateAccountStatus(@NotNull AccountDTO accountDTO) throws AccountNotFoundException {
        log.info("In updateAccountStatus()");
        Account account = repository.findById(accountDTO.id())
                .orElseThrow( () -> new AccountNotFoundException("Account with id '"+accountDTO.id()+NOT_FOUND));
        account.setStatus(accountDTO.status());
        account.setLastUpdate( new Date());

        Account accountUpdated = repository.save(account);
        log.info("account status updated");
        return mappers.fromAccount(accountUpdated);
    }

    /**
     * get customer by id from customer service
     * @param customerId of customer to retrieve
     * @return customer found or null if customer not found
     */
    private @Nullable CustomerDTO getCustomerById(String customerId){
        log.info("In getCustomerById()");
        try{
            CustomerDTO customerDTO = customerRestClient.getCustomerById(customerId);
            log.info("customer found");
            return customerDTO;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}
