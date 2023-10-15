package com.brodygaudel.accountservice.repositories;

import com.brodygaudel.accountservice.entities.Compter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompterRepository extends JpaRepository<Compter, Long> {
}
