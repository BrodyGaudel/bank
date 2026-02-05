package com.mounanga.accountqueryservice.repository;

import com.mounanga.accountqueryservice.entity.Operation;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationRepository extends JpaRepository<@NotNull Operation, @NotNull String> {

    @Query("select t from Operation t where t.account.id = :aid order by t.dateTime desc")
    Page<@NotNull Operation> findAllByAccountId(@Param("aid") String accountId, Pageable pageable);
}
