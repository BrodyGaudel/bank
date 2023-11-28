package com.brodygaudel.customerservice.repository;

import com.brodygaudel.customerservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * Checks if a customer with the given national ID (CIN) exists.
     *
     * @param cin The national ID (CIN) to check for existence.
     * @return True if a customer with the given CIN exists, false otherwise.
     */
    @Query("select case when count(c)>0 then true else false END from Customer c where c.cin=?1")
    Boolean checkIfCinExists(String cin);

    /**
     * Checks if a customer with the given phone number exists.
     *
     * @param phone The phone number to check for existence.
     * @return True if a customer with the given phone number exists, false otherwise.
     */
    @Query("select case when count(c)>0 then true else false END from Customer c where c.phone=?1")
    Boolean checkIfPhoneExists(String phone);

    /**
     * Checks if a customer with the given email address exists.
     *
     * @param email The email address to check for existence.
     * @return True if a customer with the given email address exists, false otherwise.
     */
    @Query("select case when count(c)>0 then true else false END from Customer c where c.email=?1")
    Boolean checkIfEmailExists(String email);

    /**
     * Searches for customers whose name or first name match the provided keyword.
     *
     * @param keyword  The keyword to search for in customer cin, names and first names.
     * @param pageable The pagination and sorting information.
     * @return A page of customers matching the search criteria.
     */
    @Query("SELECT c FROM Customer c WHERE c.name LIKE :keyword OR c.firstname LIKE :keyword OR c.cin LIKE :keyword ORDER BY c.cin DESC")
    Page<Customer> searchByFirstnameOrNameOrCin(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Retrieves a page of customers, ordered by their IDs.
     *
     * @param pageable The pagination and sorting information.
     * @return A page of customers ordered by ID.
     */
    @Query("SELECT c FROM Customer c ORDER BY c.cin")
    Page<Customer> getAllByPage(Pageable pageable);
}
