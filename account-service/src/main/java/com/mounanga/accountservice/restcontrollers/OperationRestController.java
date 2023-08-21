package com.mounanga.accountservice.restcontrollers;

import com.mounanga.accountservice.dtos.*;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.BalanceNotSufficientException;
import com.mounanga.accountservice.exceptions.OperationNotFoundException;
import com.mounanga.accountservice.services.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operations")
public class OperationRestController {

    private final OperationService operationService;

    public OperationRestController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("/credit")
    public CreditDTO creditAccount(@RequestBody CreditDTO creditDTO) throws AccountNotFoundException, BalanceNotSufficientException{
        return operationService.creditAccount(creditDTO);
    }

    @PostMapping("/debit")
    public DebitDTO debitAccount(@RequestBody DebitDTO debitDTO) throws AccountNotFoundException, BalanceNotSufficientException{
        return operationService.debitAccount(debitDTO);
    }

    @GetMapping("/get/{id}")
    public OperationDTO getOperationById(@PathVariable String id) throws OperationNotFoundException{
        return operationService.getOperationById(id);
    }

    @GetMapping("/{accountId}/history")
    public HistoryDTO getAccountHistory(@PathVariable String accountId,
                                        @RequestParam(name ="page", defaultValue = "0") int page,
                                        @RequestParam(name ="size", defaultValue = "5") int size) throws AccountNotFoundException{
        return operationService.getAccountHistory(accountId, page, size);
    }

    /**
     * exception handler
     * @param exception the exception to handler
     * @return the exception's message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception){
        if(exception == null){
            return new ResponseEntity<>("NULL EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
