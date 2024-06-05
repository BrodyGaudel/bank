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

    @Query("SELECT c FROM Customer c WHERE c.lastname LIKE :keyword OR c.firstname LIKE :keyword OR c.cin LIKE :keyword ORDER BY c.cin DESC")
    Page<Customer> search(@Param("keyword") String keyword, Pageable pageable);

    Optional<Customer> findByCin(String cin);

    boolean existsByCin(String cin);

    boolean existsById(@NotNull String id);

    boolean existsByEmail(String email);
}
