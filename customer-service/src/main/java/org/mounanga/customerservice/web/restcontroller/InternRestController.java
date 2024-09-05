package org.mounanga.customerservice.web.restcontroller;

import org.mounanga.customerservice.dto.CustomerExistResponseDTO;
import org.mounanga.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/intern")
public class InternRestController {

    private final CustomerService customerService;

    public InternRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/verify/{id}")
    public CustomerExistResponseDTO checkCustomerExist(@PathVariable String id){
        return customerService.checkCustomerExist(id);
    }
}
