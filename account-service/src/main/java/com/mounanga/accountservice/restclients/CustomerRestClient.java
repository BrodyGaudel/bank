package com.mounanga.accountservice.restclients;

import com.mounanga.accountservice.dtos.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient {

    @GetMapping("/bank/customers/get/{id}")
    CustomerDTO getCustomerById(@PathVariable String id);
}
