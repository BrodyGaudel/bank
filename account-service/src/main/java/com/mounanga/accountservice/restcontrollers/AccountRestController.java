package com.mounanga.accountservice.restcontrollers;


import com.mounanga.accountservice.dtos.AccountDTO;
import com.mounanga.accountservice.dtos.UpdateBalanceForm;
import com.mounanga.accountservice.dtos.UpdateStatusForm;
import com.mounanga.accountservice.exceptions.AccountNotActivatedException;
import com.mounanga.accountservice.exceptions.AccountNotFoundException;
import com.mounanga.accountservice.exceptions.AlreadyExistException;
import com.mounanga.accountservice.exceptions.CustomerNotFoundException;
import com.mounanga.accountservice.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountRestController {

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public AccountDTO createAccount(@RequestBody AccountDTO accountDTO) throws CustomerNotFoundException, AlreadyExistException {
        return accountService.createAccount(accountDTO);
    }

    @GetMapping("/get/{id}")
    public AccountDTO getAccountById(@PathVariable String id) throws AccountNotFoundException{
        return accountService.getAccountById(id);
    }

    @GetMapping("/find/{customerId}")
    public AccountDTO getAccountByCustomerId(@PathVariable String customerId) throws AccountNotFoundException{
        return accountService.getAccountByCustomerId(customerId);
    }

    @PutMapping("/update-balance")
    public AccountDTO modifyAccountBalance(@RequestBody UpdateBalanceForm form) throws AccountNotFoundException, AccountNotActivatedException {
        return accountService.modifyAccountBalance(form);
    }

    @PutMapping("/update-status")
    public AccountDTO modifyAccountStatus(@RequestBody UpdateStatusForm form) throws AccountNotFoundException{
        return accountService.modifyAccountStatus(form);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAccountById(@PathVariable String id){
        accountService.deleteAccountById(id);
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
