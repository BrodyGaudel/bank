package com.mounanga.accountqueryservice.repository;

import com.mounanga.accountqueryservice.entity.Account;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<@NotNull Account, @NotNull String> {

    @Query("select a from Account a where a.customerId =: cid")
    List<Account> findAllByCustomerId(@Param("cid") String customerId);
}
