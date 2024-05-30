package org.mounanga.customerservice.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mounanga.customerservice.dto.CustomerPageResponse;
import org.mounanga.customerservice.dto.CustomerRequest;
import org.mounanga.customerservice.dto.CustomerResponse;
import org.mounanga.customerservice.entity.Customer;
import org.mounanga.customerservice.enums.Sex;
import org.mounanga.customerservice.exception.CinAlreadyExistException;
import org.mounanga.customerservice.exception.CustomerNotFoundException;
import org.mounanga.customerservice.repository.CustomerRepository;
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
        String id = "id";
        Customer customer = Customer.builder().id(id).build();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        CustomerResponse response =  customerService.getCustomerById(id);
        assertNotNull(response);
        assertEquals(id, response.getId());
    }

    @Test
    void testGetCustomerById_ThrowsCustomerNotFoundException() {
        String id = "id";
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void testGetCustomerByCin() {
        String cin = "cin";
        Customer customer = Customer.builder().cin(cin).build();
        when(customerRepository.findByCin(cin)).thenReturn(Optional.of(customer));
        CustomerResponse response =  customerService.getCustomerByCin(cin);
        assertNotNull(response);
        assertEquals(cin, response.getCin());
    }

    @Test
    void testGetCustomerByCin_ThrowsCustomerNotFoundException() {
        String cin = "cin";
        when(customerRepository.findByCin(cin)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByCin(cin));
    }

    @Test
    void testGetAllCustomers() {
        int page = 0;
        int size = 3;
        List<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder().id("id1").build());
        customers.add(Customer.builder().id("id2").build());
        customers.add(Customer.builder().id("id3").build());
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 3);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        CustomerPageResponse response = customerService.getAllCustomers(page, size);
        assertNotNull(response);
        assertEquals(3, response.customers().size());
    }

    @Test
    void testSearchCustomers() {
        int page = 0;
        int size = 3;
        String keyword = "keyword";
        List<Customer> customers = new ArrayList<>();
        customers.add(Customer.builder().cin("keyword").firstname("John Keyword").lastname("Doe").build());
        customers.add(Customer.builder().cin("id2").lastname("keyword Doe").firstname("John").build());
        customers.add(Customer.builder().cin("keyword").firstname("John Doe").lastname("Doe").build());
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 3);

        when(customerRepository.search("%"+keyword+"%",pageable)).thenReturn(customerPage);
        CustomerPageResponse response = customerService.searchCustomers(keyword, page, size);
        assertNotNull(response);
        assertEquals(3, response.customers().size());
        assertNotNull(response.customers().get(0));
        assertNotNull(response.customers().get(1));
        assertNotNull(response.customers().get(2));
        List<CustomerResponse> customerList = response.customers();
        assertTrue(customerList.get(0).getCin().contains("keyword") || customerList.get(0).getFirstname().contains(keyword) || customerList.get(0).getLastname().contains(keyword));
        assertTrue(customerList.get(1).getCin().contains("keyword") || customerList.get(1).getFirstname().contains(keyword) || customerList.get(1).getLastname().contains(keyword));
        assertTrue(customerList.get(2).getCin().contains("keyword") || customerList.get(2).getFirstname().contains(keyword) || customerList.get(2).getLastname().contains(keyword));
    }

    @Test
    void testCreateCustomer() {
        CustomerRequest request = CustomerRequest.builder().cin("123").firstname("John").lastname("Doe").dateOfBirth(LocalDate.of(1994,1,22)).placeOfBirth("World").nationality("Gabon").sex(Sex.M).build();
        when(customerRepository.existsByCin(request.getCin())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(
                Customer.builder().cin("123").firstname("John").lastname("Doe")
                        .dateOfBirth(LocalDate.of(1994,1,22))
                        .placeOfBirth("World").nationality("Gabon").sex(Sex.M)
                        .createdDate(LocalDateTime.now()).creator("creator")
                        .build()
        );
        CustomerResponse response =  customerService.createCustomer(request);
        assertNotNull(response);
        assertEquals(request.getCin(), response.getCin());
        assertEquals(request.getFirstname(), response.getFirstname());
        assertEquals(request.getLastname(), response.getLastname());
        assertEquals(request.getDateOfBirth(), response.getDateOfBirth());
        assertEquals(request.getPlaceOfBirth(), response.getPlaceOfBirth());
        assertEquals(request.getNationality(), response.getNationality());
        assertEquals(request.getSex(), response.getSex());
        assertNotNull(response.getCreatedDate());
        assertNotNull(response.getCreator());
    }

    @Test
    void testCreateCustomer_ThrowsCinAlreadyExistException() {
        CustomerRequest request = CustomerRequest.builder().cin("123").firstname("John").lastname("Doe").dateOfBirth(LocalDate.of(1994,1,22)).placeOfBirth("World").nationality("Gabon").sex(Sex.M).build();
        when(customerRepository.existsByCin(request.getCin())).thenReturn(true);
        assertThrows(CinAlreadyExistException.class, () -> customerService.createCustomer(request));
    }

    @Test
    void testUpdateCustomer() {
        String id = "id";
        CustomerRequest request = CustomerRequest.builder().cin("123").firstname("John").lastname("Doe").dateOfBirth(LocalDate.of(1994,1,22)).placeOfBirth("World").nationality("Gabon").sex(Sex.M).build();
        Customer existingCustomer = Customer.builder().id(id).cin("Old 123").firstname("Old John").lastname("Old Doe").dateOfBirth(LocalDate.of(2000,1,22)).placeOfBirth("Old World").nationality("Old Gabon").sex(Sex.M).build();
        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCin(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(
                Customer.builder().cin(request.getCin()).firstname("John").lastname("Doe")
                        .dateOfBirth(LocalDate.of(1994,1,22))
                        .placeOfBirth("World").nationality("Gabon").sex(Sex.M)
                        .lastModifiedDate(LocalDateTime.now()).lastModifier("last modifier").id(id)
                        .build()
        );

        CustomerResponse response =  customerService.updateCustomer(id, request);
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(request.getCin(), response.getCin());
        assertEquals(request.getFirstname(), response.getFirstname());
        assertEquals(request.getLastname(), response.getLastname());
        assertEquals(request.getDateOfBirth(), response.getDateOfBirth());
        assertEquals(request.getPlaceOfBirth(), response.getPlaceOfBirth());
        assertEquals(request.getNationality(), response.getNationality());
        assertEquals(request.getSex(), response.getSex());
    }

    @Test
    void testUpdateCustomer_ThrowsCinAlreadyExistException() {
        String id = "id";
        CustomerRequest request = CustomerRequest.builder().cin("123").firstname("John").lastname("Doe").dateOfBirth(LocalDate.of(1994,1,22)).placeOfBirth("World").nationality("Gabon").sex(Sex.M).build();
        Customer existingCustomer = Customer.builder().id(id).cin("Old 123").firstname("Old John").lastname("Old Doe").dateOfBirth(LocalDate.of(2000,1,22)).placeOfBirth("Old World").nationality("Old Gabon").sex(Sex.M).build();
        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.existsByCin(anyString())).thenReturn(true);
        assertThrows(CinAlreadyExistException.class, () -> customerService.updateCustomer(id, request));
    }


    @Test
    void testDeleteCustomer() {
        String id = "id";
        customerService.deleteCustomer(id);
        verify(customerRepository, times(1)).deleteById(id);
    }
}