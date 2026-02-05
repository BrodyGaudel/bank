package com.mounanga.customercommandservice.validation.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerFieldValidatorRepository extends JpaRepository<@NotNull CustomerFieldValidator, @NotNull String> {

    boolean existsById(@NotNull String id);
    boolean existsByNip(String nip);
    boolean existsByEmail(String email);

}
