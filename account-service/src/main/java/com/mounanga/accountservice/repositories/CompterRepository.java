package com.mounanga.accountservice.repositories;

import com.mounanga.accountservice.entities.Compter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompterRepository extends JpaRepository<Compter, Long> {
}
