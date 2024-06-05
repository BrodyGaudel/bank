package org.mounanga.notificationservice.service;

import org.mounanga.notificationservice.dto.CustomerResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient {

    @GetMapping("/bank/customers/get/{id}")
    CustomerResponse getCustomer(@PathVariable String id);
}
