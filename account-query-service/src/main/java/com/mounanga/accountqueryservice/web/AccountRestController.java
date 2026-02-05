package com.mounanga.accountqueryservice.web;


import com.mounanga.accountqueryservice.entity.AccountResponseDTO;
import com.mounanga.accountqueryservice.query.GetAccountByCustomerIdQuery;
import com.mounanga.accountqueryservice.query.GetAccountByIdQuery;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountRestController {

    private final QueryGateway queryGateway;

    public AccountRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<AccountResponseDTO> getAccounts(@RequestParam(name = "customerId") UUID customerId) {
        GetAccountByCustomerIdQuery query = new GetAccountByCustomerIdQuery(customerId.toString());
        ResponseType<List<AccountResponseDTO>> responseType = ResponseTypes.multipleInstancesOf(AccountResponseDTO.class);
        return queryGateway.query(query, responseType).join();
    }

    @GetMapping("/{id}")
    public AccountResponseDTO getAccountById(@PathVariable String id) {
        GetAccountByIdQuery query = new GetAccountByIdQuery(id);
        ResponseType<AccountResponseDTO> responseType = ResponseTypes.instanceOf(AccountResponseDTO.class);
        AccountResponseDTO dto = queryGateway.query(query, responseType).join();
        if(dto == null) {
            throw new ResourceNotFoundException("Account with id "+id+" not found");
        }
        return dto;
    }
}
