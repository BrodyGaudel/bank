package org.mounanga.customerservice.web;

import jakarta.validation.Valid;
import org.mounanga.customerservice.dto.CustomerExistResponse;
import org.mounanga.customerservice.dto.CustomerPageResponse;
import org.mounanga.customerservice.dto.CustomerRequest;
import org.mounanga.customerservice.dto.CustomerResponse;
import org.mounanga.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/get/{id}")
    public CustomerResponse getCustomerById(@PathVariable String id){
        return customerService.getCustomerById(id);
    }

    @GetMapping("/find/{cin}")
    public CustomerResponse getCustomerByCin(@PathVariable String cin){
        return customerService.getCustomerByCin(cin);
    }

    @GetMapping("/list")
    public CustomerPageResponse getAllCustomers(@RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "10")  int size){
        return customerService.getAllCustomers(page, size);
    }

    @GetMapping("/search")
    public CustomerPageResponse searchCustomers(@RequestParam(name = "keyword", defaultValue = " ") String keyword,
                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size){
        return customerService.searchCustomers(keyword, page, size);
    }

    @PostMapping("/create")
    public CustomerResponse createCustomer(@RequestBody @Valid CustomerRequest request){
        return customerService.createCustomer(request);
    }

    @PutMapping("/update/{id}")
    public CustomerResponse updateCustomer(@PathVariable String id, @RequestBody @Valid CustomerRequest request){
        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable String id){
        customerService.deleteCustomer(id);
    }

    @GetMapping("/exist/{id}")
    public CustomerExistResponse checkCustomerExist(@PathVariable String id){
        return customerService.checkCustomerExist(id);
    }
}
