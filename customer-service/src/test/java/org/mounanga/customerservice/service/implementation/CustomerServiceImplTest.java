package org.mounanga.customerservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.customerservice.dto.CustomerPageResponseDTO;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;
import org.mounanga.customerservice.entity.Customer;
import org.mounanga.customerservice.enums.Gender;
import org.mounanga.customerservice.excetion.CustomerNotFoundException;
import org.mounanga.customerservice.excetion.FieldValidationException;
import org.mounanga.customerservice.repository.CustomerRepository;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void testGetCustomerById() {
        String customerId = "123";
        Customer customer = new Customer();
        customer.setId(customerId);

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));
        CustomerResponseDTO result = customerService.getCustomerById(customerId);
        assertNotNull(result);
        assertEquals(customerId, result.getId());

        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        String customerId = "123";
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(customerId));
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testGetCustomerByCin() {
        String cin = "ABC123";
        Customer customer = new Customer();
        customer.setCin(cin);

        when(customerRepository.findByCin(anyString())).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.getCustomerByCin(cin);
        assertNotNull(result);
        assertEquals(cin, result.getCin());
        verify(customerRepository, times(1)).findByCin(cin);
    }

    @Test
    void testGetCustomerByCinNotFound() {
        String cin = "ABC123";
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByCin(cin));
        verify(customerRepository, times(1)).findByCin(cin);
    }

    @Test
    void testGetAllCustomers() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Customer customer = new Customer();
        customer.setFirstname("John");
        customer.setLastname("Doe");

        Page<Customer> customersPage = new PageImpl<>(List.of(customer), pageable, 1);
        when(customerRepository.findAll(pageable)).thenReturn(customersPage);

        CustomerPageResponseDTO result = customerService.getAllCustomers(page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getCustomers().size());
        assertEquals("John", result.getCustomers().getFirst().getFirstname());

        verify(customerRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSearchCustomers(){
        String keyword = "John";
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        Customer customer1 = new Customer();
        customer1.setFirstname("Jack John");
        customer1.setLastname("Doe");

        Customer customer2 = new Customer();
        customer2.setFirstname("Marvin");
        customer2.setLastname("Orton John");

        Page<Customer> customersPage = new PageImpl<>(List.of(customer1, customer2), pageable, 2);

        when(customerRepository.search("%" + keyword + "%", pageable)).thenReturn(customersPage);

        CustomerPageResponseDTO result = customerService.searchCustomers(keyword, page, size);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getCustomers().size());
        assertTrue(result.getCustomers().getFirst().getFirstname().contains(keyword) || result.getCustomers().getFirst().getLastname().contains(keyword));
        assertTrue(result.getCustomers().getLast().getFirstname().contains(keyword) || result.getCustomers().getLast().getLastname().contains(keyword));
        verify(customerRepository, times(1)).search("%" + keyword + "%", pageable);
    }

    @Test
    void testCreateCustomer() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setFirstname("John");
        dto.setLastname("Doe");
        dto.setPlaceOfBirth("Paris");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setNationality("French");
        dto.setGender(Gender.M);
        dto.setCin("ABC123");
        dto.setEmail("john.doe@example.com");

        Customer savedCustomer = new Customer();
        savedCustomer.setId("123");
        savedCustomer.setFirstname(dto.getFirstname());
        savedCustomer.setLastname(dto.getLastname());
        savedCustomer.setCin(dto.getCin());
        savedCustomer.setEmail(dto.getEmail());

        when(customerRepository.existsByCin(dto.getCin())).thenReturn(false);
        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponseDTO result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("ABC123", result.getCin());
        assertEquals("john.doe@example.com", result.getEmail());

        verify(customerRepository, times(1)).existsByCin(dto.getCin());
        verify(customerRepository, times(1)).existsByEmail(dto.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomerCinAlreadyExists() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setCin("ABC123");
        dto.setEmail("john.doe@example.com");

        when(customerRepository.existsByCin(dto.getCin())).thenReturn(true);
        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThrows(FieldValidationException.class, () -> customerService.createCustomer(dto));

        verify(customerRepository, times(1)).existsByCin(dto.getCin());
        verify(customerRepository, times(1)).existsByEmail(dto.getEmail());
    }

    @Test
    void testCreateCustomerEmailAlreadyExists() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setCin("ABC123");
        dto.setEmail("john.doe@example.com");

        when(customerRepository.existsByCin(dto.getCin())).thenReturn(false);
        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(FieldValidationException.class, () -> customerService.createCustomer(dto));

        verify(customerRepository, times(1)).existsByCin(dto.getCin());
        verify(customerRepository, times(1)).existsByEmail(dto.getEmail());
    }

    @Test
    void testUpdateCustomer() {
        String customerId = "123";
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setFirstname("Jane");
        dto.setLastname("Doe");
        dto.setPlaceOfBirth("New York");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setNationality("American");
        dto.setGender(Gender.F);
        dto.setCin("XYZ987");
        dto.setEmail("jane.doe@example.com");

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setFirstname("John");
        existingCustomer.setLastname("Doe");
        existingCustomer.setCin("ABC123");
        existingCustomer.setEmail("john.doe@example.com");

        Customer updatedCustomer = fromCustomerRequestDTO(customerId, dto);

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCin(dto.getCin())).thenReturn(false);
        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        
        CustomerResponseDTO result = customerService.updateCustomer(customerId, dto);
        
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("Jane", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("XYZ987", result.getCin());
        assertEquals("jane.doe@example.com", result.getEmail());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByCin(dto.getCin());
        verify(customerRepository, times(1)).existsByEmail(dto.getEmail());
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void testUpdateCustomerCinAlreadyExists() {
        String customerId = "123";
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setFirstname("Jane");
        dto.setLastname("Doe");
        dto.setPlaceOfBirth("New York");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setNationality("American");
        dto.setGender(Gender.F);
        dto.setCin("XYZ987");
        dto.setEmail("jane.doe@example.com");

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setFirstname("John");
        existingCustomer.setLastname("Doe");
        existingCustomer.setCin("ABC123");
        existingCustomer.setEmail("john.doe@example.com");

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCin(dto.getCin())).thenReturn(true);
        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThrows(FieldValidationException.class, () -> customerService.updateCustomer(customerId, dto));

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByCin(dto.getCin());
        verify(customerRepository, times(1)).existsByEmail(dto.getEmail());
    }

    @Test
    void testUpdateCustomerEmailAlreadyExists() {
        String customerId = "123";
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setFirstname("Jane");
        dto.setLastname("Doe");
        dto.setPlaceOfBirth("New York");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setNationality("American");
        dto.setGender(Gender.F);
        dto.setCin("XYZ987");
        dto.setEmail("jane.doe@example.com");

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setFirstname("John");
        existingCustomer.setLastname("Doe");
        existingCustomer.setCin("ABC123");
        existingCustomer.setEmail("john.doe@example.com");

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCin(dto.getCin())).thenReturn(false);
        when(customerRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(FieldValidationException.class, () -> customerService.updateCustomer(customerId, dto));

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByCin(dto.getCin());
        verify(customerRepository, times(1)).existsByEmail(dto.getEmail());
    }

    private Customer fromCustomerRequestDTO(String customerId, CustomerRequestDTO dto){
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customerId);
        updatedCustomer.setFirstname(dto.getFirstname());
        updatedCustomer.setLastname(dto.getLastname());
        updatedCustomer.setPlaceOfBirth(dto.getPlaceOfBirth());
        updatedCustomer.setDateOfBirth(dto.getDateOfBirth());
        updatedCustomer.setNationality(dto.getNationality());
        updatedCustomer.setGender(dto.getGender());
        updatedCustomer.setCin(dto.getCin());
        updatedCustomer.setEmail(dto.getEmail());
        return updatedCustomer;
    }

}