package org.mounanga.accountservice.queries.reposiory;

import org.mounanga.accountservice.queries.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OperationRepository extends JpaRepository<Operation, String> {

    @Query("select o from Operation o where o.account.id=:id order by o.dateTime desc")
    Page<Operation> findByAccountId(@Param("id") String accountId, Pageable pageable);

    @Modifying
    @Query("delete from Operation o where o.account.id = :id")
    void deleteByAccountId(@Param("id") String accountId);
}
