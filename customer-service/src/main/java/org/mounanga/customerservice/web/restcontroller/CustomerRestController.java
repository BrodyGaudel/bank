package org.mounanga.customerservice.web.restcontroller;

import jakarta.validation.Valid;
import org.mounanga.customerservice.dto.CustomerExistResponseDTO;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;
import org.mounanga.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/get/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable String id){
        return customerService.getCustomerById(id);
    }

    @GetMapping("/list")
    public List<CustomerResponseDTO> getAllCustomers(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size){
        return customerService.getAllCustomers(page, size);
    }

    @GetMapping("/search")
    public List<CustomerResponseDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = " ") String keyword,
                                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size){
        return customerService.searchCustomers(keyword, page, size);
    }


    @PostMapping("/create")
    public CustomerResponseDTO createCustomer(@RequestBody @Valid CustomerRequestDTO dto){
        return customerService.createCustomer(dto);
    }

    @PutMapping("/update/{id}")
    public CustomerResponseDTO updateCustomer(@PathVariable String id, @RequestBody @Valid CustomerRequestDTO dto){
        return customerService.updateCustomer(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomerById(@PathVariable String id){
        customerService.deleteCustomerById(id);
    }

}
