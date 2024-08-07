package org.mounanga.customerservice.util.mapper;

import org.junit.jupiter.api.Test;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;
import org.mounanga.customerservice.entity.Customer;
import org.mounanga.customerservice.enums.Gender;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapperTest {

    @Test
    void fromCustomerRequestDTO() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setFirstname("John");
        dto.setLastname("Doe");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setPlaceOfBirth("City");
        dto.setGender(Gender.M);
        dto.setNationality("Country");
        dto.setCin("123456");
        dto.setEmail("john.doe@example.com");

        Customer customer = Mapper.fromCustomerRequestDTO(dto);

        assertNotNull(customer);
        assertEquals("John", customer.getFirstname());
        assertEquals("Doe", customer.getLastname());
        assertEquals(LocalDate.of(1990, 1, 1), customer.getDateOfBirth());
        assertEquals("City", customer.getPlaceOfBirth());
        assertEquals(Gender.M, customer.getGender());
        assertEquals("Country", customer.getNationality());
        assertEquals("123456", customer.getCin());
        assertEquals("john.doe@example.com", customer.getEmail());
    }

    @Test
    void fromCustomer() {
        Customer customer = new Customer();
        customer.setId("UUID");
        customer.setFirstname("John");
        customer.setLastname("Doe");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setPlaceOfBirth("City");
        customer.setGender(Gender.M);
        customer.setNationality("Country");
        customer.setCin("123456");
        customer.setEmail("john.doe@example.com");
        customer.setCreatedDate(LocalDateTime.now());
        customer.setCreatedBy("admin");
        customer.setLastModifiedDate(LocalDateTime.now());
        customer.setLastModifiedBy("admin");

        CustomerResponseDTO dto = Mapper.fromCustomer(customer);

        assertNotNull(dto);
        assertEquals("UUID", dto.getId());
        assertEquals("John", dto.getFirstname());
        assertEquals("Doe", dto.getLastname());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getDateOfBirth());
        assertEquals("City", dto.getPlaceOfBirth());
        assertEquals(Gender.M, dto.getGender());
        assertEquals("Country", dto.getNationality());
        assertEquals("123456", dto.getCin());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals(customer.getCreatedDate(), dto.getCreatedDate());
        assertEquals(customer.getCreatedBy(), dto.getCreatedBy());
        assertEquals(customer.getLastModifiedDate(), dto.getLastModifiedDate());
        assertEquals(customer.getLastModifiedBy(), dto.getLastModifiedBy());
    }

    @Test
    void fromListOfCustomers() {
        Customer customer = new Customer();
        customer.setId("UUID");
        customer.setFirstname("John");
        customer.setLastname("Doe");
        customer.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer.setPlaceOfBirth("City");
        customer.setGender(Gender.M);
        customer.setNationality("Country");
        customer.setCin("123456");
        customer.setEmail("john.doe@example.com");
        customer.setCreatedDate(LocalDateTime.now());
        customer.setCreatedBy("admin");
        customer.setLastModifiedDate(LocalDateTime.now());
        customer.setLastModifiedBy("admin");

        List<Customer> customers = Collections.singletonList(customer);
        List<CustomerResponseDTO> dtos = Mapper.fromListOfCustomers(customers);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());

        CustomerResponseDTO dto = dtos.getFirst();
        assertEquals("UUID", dto.getId());
        assertEquals("John", dto.getFirstname());
        assertEquals("Doe", dto.getLastname());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getDateOfBirth());
        assertEquals("City", dto.getPlaceOfBirth());
        assertEquals(Gender.M, dto.getGender());
        assertEquals("Country", dto.getNationality());
        assertEquals("123456", dto.getCin());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals(customer.getCreatedDate(), dto.getCreatedDate());
        assertEquals(customer.getCreatedBy(), dto.getCreatedBy());
        assertEquals(customer.getLastModifiedDate(), dto.getLastModifiedDate());
        assertEquals(customer.getLastModifiedBy(), dto.getLastModifiedBy());
    }
}