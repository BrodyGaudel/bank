package com.brodygaudel.accountservice.web.restcontrollers;

import com.brodygaudel.accountservice.dtos.CreditDTO;
import com.brodygaudel.accountservice.dtos.DebitDTO;
import com.brodygaudel.accountservice.dtos.HistoryDTO;
import com.brodygaudel.accountservice.dtos.OperationDTO;
import com.brodygaudel.accountservice.exceptions.AccountNotActivatedException;
import com.brodygaudel.accountservice.exceptions.AccountNotFoundException;
import com.brodygaudel.accountservice.exceptions.BalanceNotSufficientException;
import com.brodygaudel.accountservice.exceptions.OperationNotFoundException;
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
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws AccountNotFoundException, BalanceNotSufficientException, AccountNotActivatedException{
        return operationService.credit(creditDTO);
    }

    @PostMapping("/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws AccountNotFoundException, BalanceNotSufficientException, AccountNotActivatedException{
        return operationService.debit(debitDTO);
    }

    @GetMapping("/get/{id}")
    public OperationDTO getById(@PathVariable String id) throws OperationNotFoundException{
        return operationService.getById(id);
    }

    @GetMapping("/{accountId}/all")
    public HistoryDTO getHistory(@PathVariable String accountId,
                                 @RequestParam(name ="page", defaultValue = "0") int page,
                                 @RequestParam(name ="size", defaultValue = "5") int size) throws AccountNotFoundException{
        return operationService.getHistory(accountId, page, size);
    }
}
