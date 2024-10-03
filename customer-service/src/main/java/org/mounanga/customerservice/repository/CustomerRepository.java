package org.mounanga.customerservice.repository;

import org.jetbrains.annotations.NotNull;
import org.mounanga.customerservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByCin(String cin);

    boolean existsByCin(String cin);

    boolean existsByEmail(String email);

    boolean existsById(@NotNull String id);

    @Query("select c from Customer c where c.firstname like :kw or c.lastname like :kw or c.cin like :kw order by c.firstname asc")
    Page<Customer> search(@Param("kw") String keyword, Pageable pageable);
}