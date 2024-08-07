package org.mounanga.customerservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.customerservice.dto.CustomerExistResponseDTO;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;
import org.mounanga.customerservice.entity.Customer;
import org.mounanga.customerservice.enums.Gender;
import org.mounanga.customerservice.exception.CustomerNotFoundException;
import org.mounanga.customerservice.exception.FieldValidationException;
import org.mounanga.customerservice.repository.CustomerRepository;
import org.mounanga.customerservice.web.restclient.AccountRestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRestClient accountRestClient;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository, accountRestClient);
    }

    @Test
    void testGetCustomerById(){
        String id = "UUID";
        Customer customer = Customer.builder().id(id).build();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        CustomerResponseDTO response = customerService.getCustomerById(id);
        assertNotNull(response);
        assertEquals(customer.getId(), response.getId());
    }

    @Test
    void testGetCustomerByIdThrowsCustomerNotFoundException(){
        String id = "UUID";
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void testGetAllCustomers(){
        int page = 0;
        int size = 2;
        List<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder().id("UUID1").build());
        customers.add(Customer.builder().id("UUID2").build());
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        List<CustomerResponseDTO> response = customerService.getAllCustomers(page,size);
        assertNotNull(response);
        assertEquals(size, response.size());
        assertNotNull(response.getFirst());
        assertNotNull(response.getLast());
    }

    @Test
    void searchCustomers(){
        int page = 0;
        int size = 2;
        String keyword = "UUID";
        List<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder().id("UUID1").firstname("John").lastname("Doe UUID").build());
        customers.add(Customer.builder().id("UUID2").firstname("Joe UUID").lastname("JK UUID JK").build());
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = new PageImpl<>(customers);
        when(customerRepository.search("%"+keyword+"%",pageable)).thenReturn(customerPage);
        List<CustomerResponseDTO> response = customerService.searchCustomers(keyword,page,size);
        assertNotNull(response);
        assertEquals(size, response.size());
        assertNotNull(response.getFirst());
        assertNotNull(response.getLast());
        assertTrue(response.getFirst().getFirstname().contains(keyword) || response.getFirst().getLastname().contains(keyword));
        assertTrue(response.getLast().getFirstname().contains(keyword) || response.getLast().getLastname().contains(keyword));
    }

    @Test
    void testCheckCustomerExist(){
        String id = "UUID";
        Customer customer = Customer.builder().id(id).email("joh@doe.com").build();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        CustomerExistResponseDTO response = customerService.checkCustomerExist(id);
        assertNotNull(response);
        assertEquals(response.id(), id);
    }

    @Test
    void testCheckCustomerExistThrowsCustomerNotFoundException(){
        String id = "UUID";
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.checkCustomerExist(id));
    }

    @Test
    void testCreateCustomer(){
        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .firstname("John").lastname("Doe").placeOfBirth("world")
                .dateOfBirth(LocalDate.of(2000,1,1))
                .email("joh@doe.com").nationality("world").cin("CIN")
                .gender(Gender.M)
                .build();
        Customer customer = Customer.builder().id("UUID") .firstname("John").lastname("Doe").placeOfBirth("world")
                .dateOfBirth(LocalDate.of(2000,1,1))
                .email("joh@doe.com").nationality("world").cin("CIN")
                .gender(Gender.M).createdBy("system").createdDate(LocalDateTime.now()).build();

        when(customerRepository.existsByCin(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);
        CustomerResponseDTO response = customerService.createCustomer(request);
        assertNotNull(response);
        assertNotNull(response.getId());
    }

    @Test
    void testCreateCustomerThrowFieldValidationException(){
        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .firstname("John").lastname("Doe").placeOfBirth("world")
                .dateOfBirth(LocalDate.of(2000,1,1))
                .email("joh@doe.com").nationality("world").cin("CIN")
                .gender(Gender.M)
                .build();
        when(customerRepository.existsByCin(anyString())).thenReturn(true);
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(FieldValidationException.class, () -> customerService.createCustomer(request));
    }

    @Test
    void testUpdateCustomer(){
        String id = "UUID";
        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .firstname("John").lastname("Doe").placeOfBirth("world")
                .dateOfBirth(LocalDate.of(2000,1,1))
                .email("joh@doe.com").nationality("world").cin("CIN")
                .gender(Gender.M)
                .build();

        Customer existingCustomer = Customer.builder().id(id).firstname("Old John").lastname("Old Doe").placeOfBirth("Old world")
                .dateOfBirth(LocalDate.of(2000,3,3))
                .email("oldjoh@doe.com").nationality("old world").cin("old CIN")
                .gender(Gender.F).build();

        Customer updatedCustomer =  Customer.builder().id(id).firstname("John").lastname("Doe").placeOfBirth("world")
                .dateOfBirth(LocalDate.of(2000,1,1))
                .email("joh@doe.com").nationality("world").cin("CIN")
                .gender(Gender.M)
                .build();

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCin(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(updatedCustomer);

        CustomerResponseDTO response = customerService.updateCustomer(id, request);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(existingCustomer.getId(), response.getId());
        assertEquals(existingCustomer.getFirstname(), response.getFirstname());
        assertEquals(existingCustomer.getLastname(), response.getLastname());
        assertEquals(existingCustomer.getDateOfBirth(), response.getDateOfBirth());
        assertEquals(existingCustomer.getPlaceOfBirth(), response.getPlaceOfBirth());
        assertEquals(existingCustomer.getGender(), response.getGender());
        assertEquals(existingCustomer.getNationality(), response.getNationality());
    }

    @Test
    void testUpdateCustomerThrowCustomerNotFoundException() {
        String id = "UUID";
        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .firstname("John").lastname("Doe").placeOfBirth("world")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .email("joh@doe.com").nationality("world").cin("CIN")
                .gender(Gender.M)
                .build();
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(id, request));
    }

    @Test
    void testUpdateCustomerThrowFieldValidationException(){
        String id = "UUID";
        CustomerRequestDTO request = CustomerRequestDTO.builder()
                .firstname("John").lastname("Doe").placeOfBirth("world")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .email("joh@doe.com").nationality("world").cin("CIN")
                .gender(Gender.M)
                .build();
        Customer existingCustomer = Customer.builder().id(id).firstname("Old John").lastname("Old Doe").placeOfBirth("Old world")
                .dateOfBirth(LocalDate.of(2000,3,3))
                .email("oldjoh@doe.com").nationality("old world").cin("oldCIN")
                .gender(Gender.F).build();

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCin(anyString())).thenReturn(true);
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(FieldValidationException.class, () -> customerService.updateCustomer(id, request));
    }

    @Test
    void deleteCustomerById(){
        String id = "UUID";
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(Customer.builder().id(id).build()));
        customerService.deleteCustomerById(id);
        verify(accountRestClient, times(1)).deleteAccountByCustomerId(anyString());
        verify(customerRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteCustomerByIdThrowCustomerNotFoundException(){
        String id = "UUID";
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(id));
    }

}