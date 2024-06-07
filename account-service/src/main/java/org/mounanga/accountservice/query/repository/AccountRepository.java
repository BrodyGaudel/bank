package org.mounanga.accountservice.query.repository;

import org.mounanga.accountservice.query.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("select a from Account a where a.customerId =:customerId")
    Optional<Account> findByCustomerId(@Param("customerId") String customerId);
}
