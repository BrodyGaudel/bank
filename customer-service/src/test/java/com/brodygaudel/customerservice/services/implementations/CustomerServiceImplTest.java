package com.brodygaudel.customerservice.services.implementations;

import com.brodygaudel.customerservice.dtos.CustomerDTO;
import com.brodygaudel.customerservice.dtos.CustomerPageDTO;
import com.brodygaudel.customerservice.entities.Customer;
import com.brodygaudel.customerservice.enums.Sex;
import com.brodygaudel.customerservice.exceptions.CinAlreadyExistException;
import com.brodygaudel.customerservice.exceptions.CustomerNotFoundException;
import com.brodygaudel.customerservice.exceptions.EmailAlreadyExistException;
import com.brodygaudel.customerservice.exceptions.PhoneAlreadyExistException;
import com.brodygaudel.customerservice.mappers.Mappers;
import com.brodygaudel.customerservice.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private CustomerServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CustomerServiceImpl(repository, mappers);
    }

    @Test
    void save() throws PhoneAlreadyExistException, CinAlreadyExistException, EmailAlreadyExistException {

        String uuid = UUID.randomUUID().toString();
        CustomerDTO customerDTO = new CustomerDTO(
                uuid, "john", "doe", "world", new Date(), "world",
                Sex.M, uuid, uuid, uuid, LocalDateTime.now(), null);
        when(mappers.fromCustomerDTO(customerDTO)).thenReturn(
               new Customer(
                       uuid, "john", "doe", "world", new Date(), "world",
                       Sex.M, uuid, uuid, uuid, LocalDateTime.now(), null
               )
        );
        when(repository.checkIfCinExists(anyString())).thenReturn(false);
        when(repository.checkIfEmailExists(anyString())).thenReturn(false);
        when(repository.checkIfPhoneExists(anyString())).thenReturn(false);
        service.save(customerDTO);
        verify(repository, times(1)).save(any());
    }

    @Test
    void update() throws PhoneAlreadyExistException, CinAlreadyExistException, CustomerNotFoundException, EmailAlreadyExistException {
        String customerId = "123";
        Customer existingCustomer = Customer.builder()
                .id(customerId).firstname("John").name("Doe").dateOfBirth(new Date()).nationality("Nationality")
                .placeOfBirth("GABON").sex(Sex.M).cin("oldCin").phone("oldPhone").email("oldEmail")
                .creation(LocalDateTime.now()).lastUpdate(null).build();

        CustomerDTO request = new CustomerDTO(
                customerId, "John", "Doe", "GABON", new Date(),
                "Nationality", Sex.M, "cin", "email", "phone", LocalDateTime.now(), null);

        when(mappers.fromCustomerDTO(request)).thenReturn(
                Customer.builder()
                        .id(customerId).firstname("John").name("Doe").dateOfBirth(new Date()).nationality("Nationality").placeOfBirth("GABON").sex(Sex.M).cin("cin")
                        .phone("phone").email("email").creation(LocalDateTime.now()).lastUpdate(null).build()
        );
        when(mappers.fromCustomer(any())).thenReturn(
                new CustomerDTO(
                        customerId, "Marvin", "Maven", "FRANCE", new Date(),
                        "Nationality", Sex.M, "cin", "email", "phone", LocalDateTime.now(), null)
        );
        when(repository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(repository.checkIfCinExists(anyString())).thenReturn(false);
        when(repository.checkIfEmailExists(anyString())).thenReturn(false);
        when(repository.checkIfPhoneExists(anyString())).thenReturn(false);
        CustomerDTO response = service.update(customerId, request);
        verify(repository, times(1)).save(any());
        assertNotNull(response);
        assertEquals(response.id(), request.id());
        assertNotEquals(response.name(), request.name());
        assertNotEquals(response.firstname(), request.firstname());
    }

    @Test
    void getById() throws CustomerNotFoundException {
        String uuid = UUID.randomUUID().toString();
        Customer customer = new Customer();
        customer.setId(uuid);
        customer.setFirstname("John");
        customer.setName("Doe");
        customer.setNationality("WORLD");
        customer.setSex(Sex.M);
        customer.setDateOfBirth(new Date());
        customer.setPhone(uuid);
        customer.setCin(uuid);
        customer.setEmail(uuid);
        customer.setPlaceOfBirth("WORLD");
        customer.setCreation(LocalDateTime.now());
        when(repository.findById(uuid)).thenReturn(Optional.of(customer));
        when(mappers.fromCustomer(any())).thenReturn(
                new CustomerDTO(
                        uuid, "John", "Doe", "FRANCE", new Date(),
                        "Nationality", Sex.M, uuid, uuid, uuid, LocalDateTime.now(), null)
        );

        CustomerDTO customerDTO = service.getById(uuid);

        assertNotNull(customerDTO);
        assertEquals(customerDTO.id(), uuid);
        assertEquals(customerDTO.email(), customer.getEmail());
        assertEquals(customerDTO.phone(), customer.getPhone());
        assertEquals(customerDTO.cin(), customer.getPhone());
        assertEquals(customerDTO.name(), customer.getName());
        assertEquals(customerDTO.firstname(), customer.getFirstname());
    }

    @Test
    void getAll() {
        int size = 10;
        int page = 1;

        List<Customer> fakeCustomers = Arrays.asList(
                new Customer("1", "John", "Doe", "City", new Date(), "Nationality", Sex.M,
                        "123456", "john.doe@email.com", "1234567890", LocalDateTime.now(), null),
                new Customer("2", "Marvin", "Doe", "City", new Date(), "Nationality", Sex.M,
                        "654321", "marvin.doe@email.com", "9876543210", LocalDateTime.now(), null)
        );

        Page<Customer> fakePage = new PageImpl<>(fakeCustomers, PageRequest.of(page, size), fakeCustomers.size());

        when(repository.getAllByPage(PageRequest.of(page, size))).thenReturn(fakePage);


        CustomerPageDTO result = service.getAll(size, page);

        assertEquals(page, result.page());
        assertEquals(size, result.size());
        assertEquals(fakePage.getTotalPages(), result.totalPage());
        assertEquals(fakeCustomers.size(), result.customerDTOList().size());

    }

    @Test
    void search() {
        String keyword = "John";
        int size = 10;
        int page = 1;

        List<Customer> fakeCustomers = Arrays.asList(
                new Customer("1", "John", "Doe", "City", new Date(), "Nationality", Sex.M,
                        "123456", "john.doe@email.com", "1234567890", LocalDateTime.now(), null),
                new Customer("2", "Marvin", "Doe", "City", new Date(), "Nationality", Sex.M,
                        "654321", "marvin.doe@email.com", "9876543210", LocalDateTime.now(), null)
        );

        Page<Customer> fakePage = new PageImpl<>(fakeCustomers, PageRequest.of(page, size), fakeCustomers.size());

        when(repository.searchByFirstnameOrNameOrCin("%"+keyword+"%", PageRequest.of(page, size)))
                .thenReturn(fakePage);

        CustomerPageDTO result = service.search(keyword, page, size);

        assertEquals(page, result.page());
        assertEquals(size, result.size());
        assertEquals(fakePage.getTotalPages(), result.totalPage());
        assertEquals(fakeCustomers.size(), result.customerDTOList().size());
    }

    @Test
    void deleteById() {
        String customerId = "1";
        service.deleteById(customerId);
        verify(repository, times(1)).deleteById(customerId);
    }
}