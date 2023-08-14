package com.mounanga.customerservice.mappers.implementations;

import com.mounanga.customerservice.dtos.CustomerDTO;
import com.mounanga.customerservice.entities.Customer;
import com.mounanga.customerservice.entities.buiders.CustomerBuilder;
import com.mounanga.customerservice.enums.Sex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MappersImplTest {

    private MappersImpl mappers;


    @BeforeEach
    void setUp() {
        mappers = new MappersImpl();
    }


    @Test
    void testFromCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO(
                "id",
                "firstname",
                "name",
                "world",
                new Date(),
                "world",
                Sex.M,
                "cin",
                "brodygaudel@spring.io",
                "123456789",
                new Date(),
                new Date()
        );

        Customer customer1 = mappers.fromCustomerDTO(customerDTO);
        Customer customer2 = mappers.fromCustomerDTO(null);

        assertNotNull(customer1);
        assertEquals(customer1.getId(), customerDTO.id());
        assertEquals(customer1.getFirstname(), customerDTO.firstname());
        assertEquals(customer1.getName(), customerDTO.name());
        assertEquals(customer1.getNationality(), customerDTO.nationality());
        assertEquals(customer1.getPlaceOfBirth(), customerDTO.placeOfBirth());
        assertEquals(customer1.getDateOfBirth(), customerDTO.dateOfBirth());
        assertEquals(customer1.getSex(), customerDTO.sex());
        assertEquals(customer1.getEmail(), customerDTO.email());
        assertEquals(customer1.getPhone(), customerDTO.phone());
        assertEquals(customer1.getCin(), customerDTO.cin());
        assertEquals(customer1.getCreation(), customerDTO.creation());
        assertEquals(customer1.getLastUpdate(), customerDTO.lastUpdate());

        assertNull(customer2);


    }

    @Test
    void testFromCustomer() {
        Customer customer = new CustomerBuilder()
                .id("id")
                .firstname("firstname")
                .name("name")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth")
                .nationality("world")
                .cin("cin")
                .phone("phone")
                .lastUpdate(new Date())
                .email("email")
                .build();
        CustomerDTO customerDTO1 = mappers.fromCustomer(customer);
        CustomerDTO customerDTO2 = mappers.fromCustomer(null);

        assertNotNull(customer);
        assertEquals(customer.getId(), customerDTO1.id());
        assertEquals(customer.getFirstname(), customerDTO1.firstname());
        assertEquals(customer.getName(), customerDTO1.name());
        assertEquals(customer.getNationality(), customerDTO1.nationality());
        assertEquals(customer.getPlaceOfBirth(), customerDTO1.placeOfBirth());
        assertEquals(customer.getDateOfBirth(), customerDTO1.dateOfBirth());
        assertEquals(customer.getSex(), customerDTO1.sex());
        assertEquals(customer.getEmail(), customerDTO1.email());
        assertEquals(customer.getPhone(), customerDTO1.phone());
        assertEquals(customer.getCin(), customerDTO1.cin());
        assertEquals(customer.getCreation(), customerDTO1.creation());
        assertEquals(customer.getLastUpdate(), customerDTO1.lastUpdate());

        assertNull(customerDTO2);
    }

    @Test
    void testFromListOfCustomers() {
        Customer customer1 = new CustomerBuilder()
                .id("id1")
                .firstname("firstname1")
                .name("name1")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth1")
                .nationality("world1")
                .cin("cin1")
                .phone("phone1")
                .lastUpdate(new Date())
                .email("email1")
                .build();

        Customer customer2 = new CustomerBuilder()
                .id("id2")
                .firstname("firstname2")
                .name("name2")
                .dateOfBirth(new Date())
                .placeOfBirth("placeOfBirth2")
                .nationality("world2")
                .cin("cin2")
                .phone("phone2")
                .lastUpdate(new Date())
                .email("email2")
                .build();

        List<CustomerDTO> customerDTOList = mappers.fromListOfCustomers(List.of(customer1, customer2));
        List<CustomerDTO> customerDTOS = mappers.fromListOfCustomers(null);

        assertNotNull(customerDTOList);
        assertFalse(customerDTOList.isEmpty());
        assertEquals(2, customerDTOList.size());
        assertTrue(customerDTOS.isEmpty());
    }
}