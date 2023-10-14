package com.brodygaudel.customerservice.mappers;

import com.brodygaudel.customerservice.dtos.CustomerDTO;
import com.brodygaudel.customerservice.entities.Customer;
import com.brodygaudel.customerservice.enums.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MappersImplTest {

    private MappersImpl mappers;

    @BeforeEach
    void setUp() {
        mappers = new MappersImpl();
    }


    @Test
    void fromCustomer() {
        String uuid = "123abc";
        LocalDateTime dateTime = LocalDateTime.now();
        Customer request = new Customer(
                uuid, "john", "doe", "world", new Date(), "world",
                Sex.M, uuid, uuid, uuid, dateTime, dateTime
        );
        CustomerDTO response = mappers.fromCustomer(request);
        assertNotNull(response);
        assertEquals(request.getId(), response.id());
        assertEquals(request.getCin(), response.cin());
        assertEquals(request.getEmail(), response.email());
        assertEquals(request.getPhone(), response.phone());
        assertEquals(request.getName(), response.name());
        assertEquals(request.getFirstname(), response.firstname());
        assertEquals(request.getNationality(), response.nationality());
        assertEquals(request.getDateOfBirth(), response.dateOfBirth());
        assertEquals(request.getPlaceOfBirth(), response.placeOfBirth());
        assertEquals(request.getSex(), response.sex());
        assertEquals(request.getCreation(), response.creation());
        assertEquals(request.getLastUpdate(), response.lastUpdate());
    }

    @Test
    void fromCustomerDTO() {
        String uuid = "123abc";
        LocalDateTime dateTime = LocalDateTime.now();
        CustomerDTO request = new CustomerDTO(
                uuid, "john", "doe", "world", new Date(), "world",
                Sex.M, uuid, uuid, uuid, dateTime, dateTime);
        Customer response = mappers.fromCustomerDTO(request);
        assertNotNull(response);
        assertEquals(request.id(), response.getId());
        assertEquals(request.cin(), response.getCin());
        assertEquals(request.email(), response.getEmail());
        assertEquals(request.phone(), response.getPhone());
        assertEquals(request.name(), response.getName());
        assertEquals(request.firstname(), response.getFirstname());
        assertEquals(request.nationality(), response.getNationality());
        assertEquals(request.dateOfBirth(), response.getDateOfBirth());
        assertEquals(request.placeOfBirth(), response.getPlaceOfBirth());
        assertEquals(request.sex(), response.getSex());
        assertEquals(request.creation(), response.getCreation());
        assertEquals(request.lastUpdate(), response.getLastUpdate());
    }
}