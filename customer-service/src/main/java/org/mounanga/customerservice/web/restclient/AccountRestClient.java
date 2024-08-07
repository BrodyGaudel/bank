package org.mounanga.customerservice.web.restclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountRestClient {

    @DeleteMapping("/clear/{customerId}")
    void deleteAccountByCustomerId(@PathVariable String customerId);
}
