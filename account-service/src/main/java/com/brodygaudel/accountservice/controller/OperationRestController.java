package com.brodygaudel.accountservice.controller;

import com.brodygaudel.accountservice.dto.CreditDTO;
import com.brodygaudel.accountservice.dto.DebitDTO;
import com.brodygaudel.accountservice.dto.HistoryDTO;
import com.brodygaudel.accountservice.dto.OperationDTO;
import com.brodygaudel.accountservice.exception.AccountNotActivatedException;
import com.brodygaudel.accountservice.exception.AccountNotFoundException;
import com.brodygaudel.accountservice.exception.BalanceNotSufficientException;
import com.brodygaudel.accountservice.exception.OperationNotFoundException;
import com.brodygaudel.accountservice.service.OperationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/operations")
public class OperationRestController {

    private final OperationService operationService;

    public OperationRestController(OperationService operationService) {
        this.operationService = operationService;
    }


    @PostMapping("/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws AccountNotFoundException, BalanceNotSufficientException, AccountNotActivatedException {
        return operationService.credit(creditDTO);
    }

    @PostMapping("/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws AccountNotFoundException, BalanceNotSufficientException, AccountNotActivatedException{
        return operationService.debit(debitDTO);
    }

    @GetMapping("/get/{id}")
    public OperationDTO getById(@PathVariable String id) throws OperationNotFoundException {
        return operationService.getById(id);
    }

    @GetMapping("/{accountId}/all")
    public HistoryDTO getHistory(@PathVariable String accountId,
                                 @RequestParam(name ="page", defaultValue = "0") int page,
                                 @RequestParam(name ="size", defaultValue = "5") int size) throws AccountNotFoundException{
        return operationService.getHistory(accountId, page, size);
    }
}
