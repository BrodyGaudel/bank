package com.mounanga.customerservice.restcontrollers;

import com.mounanga.customerservice.dtos.CustomerDTO;
import com.mounanga.customerservice.exceptions.AlreadyExistException;
import com.mounanga.customerservice.exceptions.CustomerNotFoundException;
import com.mounanga.customerservice.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public CustomerDTO getCustomerById(@PathVariable String id) throws CustomerNotFoundException {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/find/{cin}")
    public CustomerDTO getCustomerByCin(@PathVariable String cin) throws CustomerNotFoundException{
        return customerService.getCustomerByCin(cin);
    }

    @GetMapping("/list/{size}/{page}")
    public List<CustomerDTO> getAllCustomers(@PathVariable(name = "size") int size, @PathVariable(name = "page") int page){
        return customerService.getAllCustomers(size, page);
    }

    @GetMapping("/{size}/{page}/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                             @PathVariable(name = "size") int size,
                                             @PathVariable(name = "page") int page){
        return customerService.searchCustomers("%"+keyword+"%", size, page);
    }

    @PostMapping("/create")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) throws AlreadyExistException{
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/update/{id}")
    public CustomerDTO updateCustomer(@PathVariable String id, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException, AlreadyExistException{
        return customerService.updateCustomer(id, customerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomerById(@PathVariable String id){
        customerService.deleteCustomerById(id);
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
