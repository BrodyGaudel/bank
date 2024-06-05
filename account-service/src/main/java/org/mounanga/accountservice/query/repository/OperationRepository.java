package org.mounanga.accountservice.query.repository;

import org.mounanga.accountservice.query.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationRepository extends JpaRepository<Operation, String> {

    @Query("select o from Operation o where o.account.id =:accountId order by o.dateTime desc")
    Page<Operation> findByAccountId(@Param("accountId") String accountId, Pageable pageable);

}
