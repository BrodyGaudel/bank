package com.mounanga.accountcommandservice.bank;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RIBRepository extends JpaRepository<@NotNull RIB, @NotNull Long> {
    boolean existsByRibNumber(String ribNumber);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM ribs WHERE rib_number = :ribNumber)",
            nativeQuery = true)
    boolean checkExistsFast(String ribNumber);
}

