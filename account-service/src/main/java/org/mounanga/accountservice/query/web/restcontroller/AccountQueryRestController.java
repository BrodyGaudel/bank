package org.mounanga.accountservice.query.web.restcontroller;

import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.mounanga.accountservice.query.dto.AccountResponse;
import org.mounanga.accountservice.query.dto.OperationResponse;
import org.mounanga.accountservice.query.exception.AccountNotFoundException;
import org.mounanga.accountservice.query.exception.OperationNotFoundException;
import org.mounanga.accountservice.query.queries.GetAccountByIdQuery;
import org.mounanga.accountservice.query.queries.GetAccountByCustomerIdQuery;
import org.mounanga.accountservice.query.queries.GetAllOperationByAccountIdQuery;
import org.mounanga.accountservice.query.queries.GetOperationByIdQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts/queries")
public class AccountQueryRestController {

    private final QueryGateway queryGateway;

    public AccountQueryRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get-account/{id}")
    public AccountResponse getAccountById(@PathVariable String id){
        GetAccountByIdQuery query = new GetAccountByIdQuery(id);
        ResponseType<AccountResponse> responseType = ResponseTypes.instanceOf(AccountResponse.class);
        AccountResponse account = queryGateway.query(query , responseType).join();
        if(account == null){
            throw new AccountNotFoundException("account with id '"+id+"' not found.");
        }
        return account;
    }

    @GetMapping("/getaccountbycustomer/{customerId}")
    public AccountResponse getAllAccountsByCustomerId(@PathVariable String customerId){
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery(customerId);
        ResponseType<AccountResponse> responseType = ResponseTypes.instanceOf(AccountResponse.class);
        AccountResponse account = queryGateway.query(query , responseType).join();
        if(account == null){
            throw new AccountNotFoundException("account with  customerId '"+customerId+"' not found");
        }
        return account;
    }

    @GetMapping("/get-operation/{id}")
    public OperationResponse getOperationById(@PathVariable String id){
        GetOperationByIdQuery query = new GetOperationByIdQuery(id);
        ResponseType<OperationResponse> responseType = ResponseTypes.instanceOf(OperationResponse.class);
        OperationResponse operation = queryGateway.query(query , responseType).join();
        if(operation == null){
            throw new OperationNotFoundException("operation with id '"+id+"' not found");
        }
        return operation;
    }

    @GetMapping("/get-all-operations")
    public List<OperationResponse> getAllOperationByAccountId(@RequestParam(name = "accountId") String accountId,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size){
        GetAllOperationByAccountIdQuery query = new GetAllOperationByAccountIdQuery(accountId, page, size);
        ResponseType<List<OperationResponse>> responseType = ResponseTypes.multipleInstancesOf(OperationResponse.class);
        return queryGateway.query(query , responseType).join();
    }

}
