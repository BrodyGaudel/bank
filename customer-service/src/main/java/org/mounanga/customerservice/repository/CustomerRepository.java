package org.mounanga.customerservice.repository;

import org.mounanga.customerservice.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("select c from Customer c where c.firstname like :keyword or c.lastname like :keyword or c.cin like:keyword")
    Page<Customer> search(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByCin(String cin);
    boolean existsByEmail(String email);
}
