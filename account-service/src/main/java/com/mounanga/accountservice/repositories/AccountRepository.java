package com.mounanga.accountservice.repositories;

import com.mounanga.accountservice.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The <code>AccountRepository</code> interface provides data access methods for managing accounts.
 * It extends the <code>JpaRepository</code> interface for basic CRUD operations on accounts.
 *
 * @see JpaRepository
 * @see Account
 */
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Retrieves an account based on the provided customer ID.
     *
     * @param customerId The unique identifier of the customer.
     * @return The {@link Account} associated with the given customer ID, or {@code null} if not found.
     */
    @Query("SELECT c FROM Account c WHERE c.customerId = ?1")
    Account findByCustomerId(String customerId);

    /**
     * Checks if an account with the specified customer ID exists.
     *
     * @param customerId The unique identifier of the customer.
     * @return {@code true} if an account with the given customer ID exists, otherwise {@code false}.
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.customerId = ?1")
    Boolean checkIfCustomerIdExists(String customerId);
}

