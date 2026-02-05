package com.mounanga.customerqueryservice.repository;

import com.mounanga.customerqueryservice.entity.Customer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<@NotNull Customer, @NotNull String> {

    Optional<Customer> findByNip(String nip);

    Optional<Customer> findByEmail(String email);


    //boolean existsById(String id);

    @Query("select c from Customer c where c.isDeleted=false and (c.firstname like :kw or c.lastname like: kw or c.nip like:kw)")
    Page<@NotNull Customer> findByKeyword(@Param("kw") String keyword, Pageable pageable);
}
