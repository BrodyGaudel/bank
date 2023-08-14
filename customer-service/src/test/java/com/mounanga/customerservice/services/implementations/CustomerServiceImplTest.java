package com.mounanga.customerservice.services.implementations;

import com.mounanga.customerservice.dtos.CustomerDTO;
import com.mounanga.customerservice.entities.Customer;
import com.mounanga.customerservice.entities.buiders.CustomerBuilder;
import com.mounanga.customerservice.enums.Sex;
import com.mounanga.customerservice.exceptions.AlreadyExistException;
import com.mounanga.customerservice.exceptions.CustomerNotFoundException;
import com.mounanga.customerservice.mappers.implementations.MappersImpl;
import com.mounanga.customerservice.repositories.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {

    private CustomerServiceImpl service;

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        service = new CustomerServiceImpl(repository, new MappersImpl());
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }


    @Test
    void testGetCustomerById() throws CustomerNotFoundException {
        String unique = UUID.randomUUID().toString();
        Customer customer = new CustomerBuilder()
                .id(unique)
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin(unique)
                .phone(unique)
                .lastUpdate(new Date())
                .email(unique)
                .build();
        Customer customerSaved = repository.save(customer);
        CustomerDTO customerDTO = service.getCustomerById(customerSaved.getId());
        assertNotNull(customerDTO);
        assertEquals(customerDTO.id(), customerSaved.getId());
    }

    @Test
    void testGetCustomerByCin() throws CustomerNotFoundException {
        String unique = UUID.randomUUID().toString();
        Customer customer = new CustomerBuilder()
                .id(unique)
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin(unique)
                .phone(unique)
                .lastUpdate(new Date())
                .email(unique)
                .build();
        Customer customerSaved = repository.save(customer);
        CustomerDTO customerDTO = service.getCustomerByCin(customerSaved.getCin());
        assertNotNull(customerDTO);
        assertEquals(customerDTO.id(), customerSaved.getId());
    }

    @Test
    void testGetAllCustomers() {
        String unique1 = UUID.randomUUID().toString();
        Customer customer1 = new CustomerBuilder()
                .id(unique1)
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin(unique1)
                .phone(unique1)
                .lastUpdate(new Date())
                .email(unique1)
                .build();

        String unique2 = UUID.randomUUID().toString();
        Customer customer2 = new CustomerBuilder()
                .id(unique2)
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin(unique2)
                .phone(unique2)
                .lastUpdate(new Date())
                .email(unique2)
                .build();
        repository.saveAll(List.of(customer1, customer2));
        List<CustomerDTO> resultPage = service.getAllCustomers(2,0);
        assertNotNull(resultPage);
        assertEquals(2, resultPage.size());
    }

    @Test
    void testSearchCustomers() {
        String unique1 = UUID.randomUUID().toString();
        Customer customer1 = new CustomerBuilder()
                .id(unique1)
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin(unique1)
                .phone(unique1)
                .lastUpdate(new Date())
                .email(unique1)
                .build();

        String unique2 = UUID.randomUUID().toString();
        Customer customer2 = new CustomerBuilder()
                .id(unique2)
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin(unique2)
                .phone(unique2)
                .lastUpdate(new Date())
                .email(unique2)
                .build();
        repository.saveAll(List.of(customer1, customer2));

        String keywork = "first";

        List<CustomerDTO> customerDTOList = service.searchCustomers("%"+keywork+"%",2,0);
        assertNotNull(customerDTOList);
        assertEquals(2, customerDTOList.size());
        assertTrue(customerDTOList.get(0).firstname().contains("first"));
    }

    @Test
    void testSaveCustomer() throws AlreadyExistException {
        String unique = UUID.randomUUID().toString();
        CustomerDTO request = new CustomerDTO(
                "id",
                "firstname",
                "name",
                "placeOfBirth",
                new Date(),
                "nationality",
                Sex.M,
                unique,
                unique,
                unique,
                new Date(),
                new Date()
        );
        CustomerDTO response = service.saveCustomer(request);
        assertNotNull(response);
        assertEquals(response.cin(), request.cin());
        assertEquals(response.email(), request.email());
        assertEquals(response.phone(), request.phone());
    }

    @Test
    void testUpdateCustomer() throws CustomerNotFoundException, AlreadyExistException {
        String unique = UUID.randomUUID().toString();
        Customer customer = new CustomerBuilder()
                .id(unique)
                .firstname("firstname")
                .name("the name")
                .dateOfBirth(new Date())
                .placeOfBirth("France")
                .nationality("Canada")
                .cin(unique)
                .phone(unique)
                .lastUpdate(new Date())
                .email(unique)
                .build();
        Customer customerSaved = repository.save(customer);

        CustomerDTO customerDTO = service.updateCustomer(
               customerSaved.getId(),
               new CustomerDTO(
                       "id",
                       "nom",
                       "prenom",
                       "Gabon",
                       new Date(),
                       "CHINA",
                       Sex.M,
                       "123CIN456789",
                       "bank@spring.io",
                       "987654321",
                       new Date(),
                       new Date()
               )
        );

       assertNotNull(customerDTO);

       assertEquals(customerDTO.id(), customerSaved.getId());
       assertNotEquals(customerDTO.name(), customerSaved.getName());
       assertNotEquals(customerDTO.firstname(), customerSaved.getFirstname());
       assertNotEquals(customerDTO.nationality(), customerSaved.getNationality());
       assertNotEquals(customerDTO.placeOfBirth(), customerSaved.getPlaceOfBirth());
       assertNotEquals(customerDTO.email(), customerSaved.getEmail());
       assertNotEquals(customerDTO.phone(), customerSaved.getPhone());
       assertNotEquals(customerDTO.cin(), customerSaved.getCin());
    }

    @Test
    void testDeleteCustomerById() {
        String unique = UUID.randomUUID().toString();
        Customer customer = new CustomerBuilder()
                .id(unique)
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin(unique)
                .phone(unique)
                .lastUpdate(new Date())
                .email(unique)
                .build();
        Customer customerSaved = repository.save(customer);
        service.deleteCustomerById(customerSaved.getId());
        Customer response = repository.findById(customerSaved.getId()).orElse(null);
        assertNull(response);
    }
}