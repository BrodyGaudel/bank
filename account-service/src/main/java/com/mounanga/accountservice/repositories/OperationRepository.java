package com.mounanga.accountservice.repositories;

import com.mounanga.accountservice.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface OperationRepository extends JpaRepository<Operation, String> {

    @Query("select o from Operation o where o.account.id = ?1 order by o.date desc")
    Page<Operation> findByAccountIdOrderByDateDesc(String accountId, Pageable pageable);
}
