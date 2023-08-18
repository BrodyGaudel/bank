package com.mounanga.operationservice.repositories;

import com.mounanga.operationservice.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, String> {
    @Query("select op from Operation op where op.accountId =?1")
    List<Operation> findByAccountId(String accountId);

    @Query("SELECT op FROM Operation op WHERE op.accountId = :accountId ORDER BY op.date DESC")
    Page<Operation> findByAccountIdOrderByDateDesc(@Param("accountId") String accountId, Pageable pageable);
}
