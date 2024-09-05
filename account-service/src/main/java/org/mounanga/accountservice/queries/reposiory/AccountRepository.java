package org.mounanga.accountservice.queries.reposiory;

import org.mounanga.accountservice.queries.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("select a from Account a where a.customerId =:id")
    Optional<Account> findByCustomerId(@Param("id") String customerId);
}
