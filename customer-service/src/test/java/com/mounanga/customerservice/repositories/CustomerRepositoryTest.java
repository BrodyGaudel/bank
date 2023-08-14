package com.mounanga.customerservice.repositories;

import com.mounanga.customerservice.entities.Customer;
import com.mounanga.customerservice.entities.buiders.CustomerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testCheckIfCinExists() {
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
        Boolean check1 = repository.checkIfCinExists(customerSaved.getCin());
        Boolean check2 = repository.checkIfCinExists("cin");
        assertEquals(Boolean.TRUE, check1);
        assertNotEquals(Boolean.TRUE, check2);
    }

    @Test
    void testCheckIfPhoneExists() {
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
        Boolean check1 = repository.checkIfPhoneExists(customerSaved.getPhone());
        Boolean check2 = repository.checkIfCinExists("phone");
        assertEquals(Boolean.TRUE, check1);
        assertNotEquals(Boolean.TRUE, check2);
    }

    @Test
    void testCheckIfEmailExists() {
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
        Boolean check1 = repository.checkIfEmailExists(customerSaved.getEmail());
        Boolean check2 = repository.checkIfCinExists("email");
        assertEquals(Boolean.TRUE, check1);
        assertNotEquals(Boolean.TRUE, check2);
    }

    @Test
    void testSearchByFirstnameOrName() {
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
        String keyword = customerSaved.getFirstname();
        Pageable pageable = PageRequest.of(0, 2);
        Page<Customer> resultPage = repository.searchByFirstnameOrName("%"+keyword+"%", pageable);
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    void testFindByCin() {
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
        Customer response = repository.findByCin(customerSaved.getCin());
        assertNotNull(response);
        assertEquals(response.getCin(), customerSaved.getCin());
        assertEquals(response.getId(), customerSaved.getId());
    }

    @Test
    void testGetAllByPage() {
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
        Pageable pageable = PageRequest.of(0, 2);
        Page<Customer> resultPage = repository.getAllByPage(pageable);
        assertNotNull(resultPage);
        assertEquals(2, resultPage.getTotalElements());
    }
}