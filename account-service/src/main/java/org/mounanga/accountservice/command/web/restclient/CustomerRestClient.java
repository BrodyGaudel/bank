package org.mounanga.accountservice.command.web.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient {

    @GetMapping("/bank/customers/exist/{id}")
    CustomerExistResponse customerExist(@PathVariable String id);
}
