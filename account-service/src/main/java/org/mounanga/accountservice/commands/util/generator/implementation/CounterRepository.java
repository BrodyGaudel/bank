package org.mounanga.accountservice.commands.util.generator.implementation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Long> {
}
