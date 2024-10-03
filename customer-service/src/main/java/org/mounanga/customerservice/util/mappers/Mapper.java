package org.mounanga.customerservice.util.mappers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.mounanga.customerservice.dto.CustomerPageResponseDTO;
import org.mounanga.customerservice.dto.CustomerRequestDTO;
import org.mounanga.customerservice.dto.CustomerResponseDTO;
import org.mounanga.customerservice.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Utility class for mapping between DTOs and Entities in the customer service.
 * This class provides static methods to convert between {@link CustomerRequestDTO},
 * {@link CustomerResponseDTO}, and {@link Customer} objects.
 * <p>
 * This class cannot be instantiated.
 * </p>
 *
 * <p><b>Author:</b> Brody Gaudel MOUNANGA BOUKA</p>
 */
public class Mapper {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should only be used statically.
     */
    private Mapper() {
        super();
    }

    /**
     * Converts a {@link CustomerRequestDTO} to a {@link Customer} entity.
     *
     * @param dto the {@link CustomerRequestDTO} object containing customer data to map.
     * @return a {@link Customer} entity with the values from the DTO.
     * @throws NullPointerException if the input {@code dto} is null.
     */
    public static @NotNull Customer fromCustomer(final @NotNull CustomerRequestDTO dto){
        final Customer customer = new Customer();
        customer.setFirstname(dto.getFirstname());
        customer.setLastname(dto.getLastname());
        customer.setPlaceOfBirth(dto.getPlaceOfBirth());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setGender(dto.getGender());
        customer.setNationality(dto.getNationality());
        customer.setCin(dto.getCin());
        customer.setEmail(dto.getEmail());
        return customer;
    }

    /**
     * Converts a {@link Customer} entity to a {@link CustomerResponseDTO}.
     *
     * @param customer the {@link Customer} entity to map.
     * @return a {@link CustomerResponseDTO} object with values from the customer entity.
     * @throws NullPointerException if the input {@code customer} is null.
     */
    public static CustomerResponseDTO fromCustomer(final @NotNull Customer customer){
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .firstname(customer.getFirstname())
                .lastname(customer.getLastname())
                .placeOfBirth(customer.getPlaceOfBirth())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .nationality(customer.getNationality())
                .cin(customer.getCin())
                .email(customer.getEmail())
                .createdDate(customer.getCreatedDate())
                .createdBy(customer.getCreatedBy())
                .lastModifiedDate(customer.getLastModifiedDate())
                .lastModifiedBy(customer.getLastModifiedBy())
                .build();
    }

    /**
     * Converts a list of {@link Customer} entities to a list of {@link CustomerResponseDTO}.
     *
     * @param customers the list of {@link Customer} entities to map.
     * @return a list of {@link CustomerResponseDTO} objects corresponding to the input list.
     * @throws NullPointerException if the input {@code customers} list is null.
     */
    public static List<CustomerResponseDTO> fromListOfCustomers(final @NotNull List<Customer> customers){
        return customers.stream().map(Mapper::fromCustomer).toList();
    }

    /**
     * Converts a {@link Page} of {@link Customer} entities to a {@link CustomerPageResponseDTO}.
     * This method maps the paginated list of customers along with pagination details.
     *
     * @param pageOfCustomers the {@link Page} object containing the list of {@link Customer} entities and pagination information.
     * @return a {@link CustomerPageResponseDTO} containing the list of {@link CustomerResponseDTO} and pagination details.
     * @throws NullPointerException if the input {@code pageOfCustomers} is null.
     */
    public static CustomerPageResponseDTO fromPageOfCustomers(final @NotNull Page<Customer> pageOfCustomers){
        return CustomerPageResponseDTO.builder()
                .customers(fromListOfCustomers(pageOfCustomers.getContent()))
                .totalPages(pageOfCustomers.getTotalPages())
                .totalElements(pageOfCustomers.getTotalElements())
                .size(pageOfCustomers.getSize())
                .numberOfElements(pageOfCustomers.getNumberOfElements())
                .number(pageOfCustomers.getNumber())
                .hasContent(pageOfCustomers.hasContent())
                .isFirst(pageOfCustomers.isFirst())
                .isLast(pageOfCustomers.isLast())
                .hasPrevious(pageOfCustomers.hasPrevious())
                .hasNext(pageOfCustomers.hasNext())
                .build();
    }


    /**
     * Updates the properties of an existing {@link Customer} entity using the values from a {@link CustomerRequestDTO}.
     * This method modifies the provided {@code customer} object with the values from the {@code dto} and returns the updated customer.
     *
     * <p>
     * The method is annotated with {@link Contract} to indicate that the returned object is always the same as the {@code customer} parameter.
     * This means the method guarantees that the same {@code customer} object passed in will be returned, after being modified.
     * </p>
     *
     * @param customer the existing {@link Customer} entity to update.
     * @param dto the {@link CustomerRequestDTO} containing the new values for the customer.
     * @return the same {@link Customer} entity passed in as {@code customer}, with updated fields.
     * @throws NullPointerException if either {@code customer} or {@code dto} is null.
     */
    @Contract("_, _ -> param1")
    public static @NotNull Customer updateCustomerItems(@NotNull Customer customer, final @NotNull CustomerRequestDTO dto){
        customer.setFirstname(dto.getFirstname());
        customer.setGender(dto.getGender());
        customer.setNationality(dto.getNationality());
        customer.setCin(dto.getCin());
        customer.setEmail(dto.getEmail());
        customer.setLastname(dto.getLastname());
        customer.setPlaceOfBirth(dto.getPlaceOfBirth());
        customer.setDateOfBirth(dto.getDateOfBirth());
        return customer;
    }
}