package com.brodygaudel.accountservice.repository;

import com.brodygaudel.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Retrieves an account based on the given customer ID.
     *
     * @param customerId The customer ID to search for.
     * @return The account associated with the given customer ID, or null if not found.
     */
    @Query("select a from Account a where a.customerId = ?1")
    Account findByCustomerId(String customerId);

    /**
     * Checks if an account with the specified customer ID exists.
     *
     * @param customerId The customer ID to check.
     * @return True if an account with the given customer ID exists, false otherwise.
     */
    @Query("select case when count(a) > 0 then true else false end from Account a where a.customerId = ?1")
    Boolean checkIfCustomerIdExists(String customerId);
}
