package com.mounanga.accountservice.services.implementations;

import com.mounanga.accountservice.dtos.*;
import com.mounanga.accountservice.entities.Account;
import com.mounanga.accountservice.entities.Operation;
import com.mounanga.accountservice.entities.builders.OperationBuilder;
import com.mounanga.accountservice.enums.OperationType;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.BalanceNotSufficientException;
import com.mounanga.accountservice.exceptions.OperationNotFoundException;
import com.mounanga.accountservice.mappers.Mappers;
import com.mounanga.accountservice.services.OperationService;
import com.mounanga.accountservice.repositories.AccountRepository;
import com.mounanga.accountservice.repositories.OperationRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class OperationServiceImpl implements OperationService {

    private static final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    private static final String NOT_FOUND = "' not found.";
    private static final String ACCOUNT_WITH_ID = "Account with id '";

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;
    private final Mappers mappers;

    public OperationServiceImpl(OperationRepository operationRepository, AccountRepository accountRepository, Mappers mappers) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
        this.mappers = mappers;
    }

    /**
     * Credit funds to an account.
     *
     * @param creditDTO The credit details.
     * @return The updated account details after the credit operation.
     * @throws AccountNotFoundException      If the account for the credit operation is not found.
     * @throws BalanceNotSufficientException If the account balance is not sufficient for the credit operation.
     */
    @Override
    public CreditDTO creditAccount(@NotNull CreditDTO creditDTO) throws AccountNotFoundException, BalanceNotSufficientException {
        log.info("In creditAccount()");
        Account account = accountRepository.findById(creditDTO.id())
                .orElseThrow( () -> new AccountNotFoundException(ACCOUNT_WITH_ID+creditDTO.id()+NOT_FOUND));
        if (creditDTO.amount() == null || creditDTO.amount().compareTo(BigDecimal.ZERO) < 0){
            throw new BalanceNotSufficientException("Balance Not Sufficient : amount must be non-null and greater than zero");
        }

        //update account balance
        BigDecimal amount = account.getBalance().add(creditDTO.amount());
        account.setBalance(amount);
        account.setLastUpdate( new Date());
        Account accountUpdated = accountRepository.save(account);

        //save operation
        Operation operation = new OperationBuilder()
                .setId(UUID.randomUUID().toString())
                .setCurrency(account.getCurrency())
                .setType(OperationType.CREDIT)
                .setDescription(creditDTO.description())
                .setDate(accountUpdated.getLastUpdate())
                .setAmount(creditDTO.amount())
                .setAccount(accountUpdated)
                .build();
        Operation operationSaved = operationRepository.save(operation);
        log.info("credit operation success");

        return new CreditDTO(operation.getAccount().getId(), operationSaved.getDescription(), operationSaved.getAmount());
    }

    /**
     * Debit funds from an account.
     *
     * @param debitDTO The debit details.
     * @return The updated account details after the debit operation.
     * @throws AccountNotFoundException      If the account for the debit operation is not found.
     * @throws BalanceNotSufficientException If the account balance is not sufficient for the debit operation.
     */
    @Override
    public DebitDTO debitAccount(@NotNull DebitDTO debitDTO) throws AccountNotFoundException, BalanceNotSufficientException {
        log.info("In debitAccount()");
        Account account = accountRepository.findById(debitDTO.id())
                .orElseThrow( () -> new AccountNotFoundException(ACCOUNT_WITH_ID+debitDTO.id()+NOT_FOUND));
        if (debitDTO.amount() == null || debitDTO.amount().compareTo(account.getBalance()) < 0){
            throw new BalanceNotSufficientException("Balance Not Sufficient : amount must be non-null and greater than account balance");
        }
        BigDecimal amount = account.getBalance().subtract(debitDTO.amount());
        account.setBalance(amount);
        account.setLastUpdate( new Date());
        Account accountUpdated = accountRepository.save(account);

        //save operation
        Operation operation = new OperationBuilder()
                .setId(UUID.randomUUID().toString())
                .setCurrency(account.getCurrency())
                .setType(OperationType.DEBIT)
                .setDescription(debitDTO.description())
                .setDate(accountUpdated.getLastUpdate())
                .setAmount(debitDTO.amount())
                .setAccount(accountUpdated)
                .build();
        Operation operationSaved = operationRepository.save(operation);
        log.info("debit operation success");

        return new DebitDTO(operation.getAccount().getId(), operationSaved.getDescription(), operationSaved.getAmount());
    }


    /**
     * Retrieve an operation by its ID.
     *
     * @param id The ID of the operation to retrieve.
     * @return The operation details.
     * @throws OperationNotFoundException If the operation with the given ID is not found.
     */
    @Override
    public OperationDTO getOperationById(String id) throws OperationNotFoundException {
        log.info("In getOperationById()");
        Operation operation = operationRepository.findById(id)
                .orElseThrow( () -> new OperationNotFoundException("Operation wit id '"+id+NOT_FOUND));
        log.info("operation found");
        return mappers.fromOperation(operation);
    }

    /**
     * Retrieve the history of operations for a specific account.
     *
     * @param accountId The ID of the account to retrieve the history for.
     * @param page      The page number for pagination.
     * @param size      The number of operations per page.
     * @return The history of operations for the account.
     * @throws AccountNotFoundException if account not found
     */
    @Override
    public HistoryDTO getAccountHistory(String accountId, int page, int size) throws AccountNotFoundException {
        log.info("In getAccountHistory()");
        Account account = accountRepository.findById(accountId)
                .orElseThrow( () -> new AccountNotFoundException(ACCOUNT_WITH_ID+accountId+NOT_FOUND));

        Page<Operation> operationPage = operationRepository.findByAccountIdOrderByDateDesc(account.getId(), PageRequest.of(page, size));
        List<OperationDTO> operationDTOList = operationPage.getContent()
                .stream()
                .map(mappers::fromOperation)
                .toList();

        log.info("history found");
        return new HistoryDTO(
                account.getId(),
                account.getCurrency(),
                account.getBalance(),
                page,
                operationPage.getTotalPages(),
                size,
                operationDTOList
        );
    }
}
